package com.example.bankingapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanQrActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private var camera: Camera? = null
    private var isFlashOn = false
    private lateinit var flashToggle: ImageView
    private lateinit var galleryButton: ImageView
    private lateinit var myQrButton: ImageView
    private lateinit var contactButton: ImageView
    private lateinit var inputMobileOrName: EditText

    // Activity result launchers
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { scanQrFromImage(it) }
    }

    private val contactLauncher = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let { handleContactSelection(it) }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.CAMERA] == true) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scan_qr)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupClickListeners()
        checkPermissionsAndStartCamera()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun initializeViews() {
        flashToggle = findViewById(R.id.flashToggle)
        galleryButton = findViewById(R.id.galleryButton)
        myQrButton = findViewById(R.id.myQrButton)
        inputMobileOrName = findViewById(R.id.inputMobileOrName)

        // Get contact button from the drawable end of EditText
        contactButton = inputMobileOrName.compoundDrawables[2] as? ImageView
            ?: ImageView(this) // Fallback if drawable not found
    }

    private fun setupClickListeners() {
        // Flash toggle
        flashToggle.setOnClickListener {
            toggleFlash()
        }

        // Gallery button
        galleryButton.setOnClickListener {
            openGallery()
        }

        // My QR button
        myQrButton.setOnClickListener {
            showMyQrCode()
        }

        // Contact selection (on EditText drawable click)
        inputMobileOrName.setOnTouchListener { _, event ->
            val drawableEnd = 2
            if (event.rawX >= (inputMobileOrName.right - inputMobileOrName.compoundDrawables[drawableEnd].bounds.width())) {
                openContactPicker()
                true
            } else {
                false
            }
        }
    }

    private fun checkPermissionsAndStartCamera() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            permissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // QR Code analyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrCodeAnalyzer { qrCode ->
                        runOnUiThread {
                            handleQrCodeDetected(qrCode)
                        }
                    })
                }

            preview.setSurfaceProvider(findViewById<PreviewView>(R.id.previewView).surfaceProvider)

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun toggleFlash() {
        camera?.let { cam ->
            if (cam.cameraInfo.hasFlashUnit()) {
                isFlashOn = !isFlashOn
                cam.cameraControl.enableTorch(isFlashOn)

                // Update flash icon
                flashToggle.setImageResource(
                    if (isFlashOn) R.drawable.flashlight_on
                    else R.drawable.flashlight
                )
            } else {
                Toast.makeText(this, "Flash not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun scanQrFromImage(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val image = InputImage.fromBitmap(bitmap, 0)

            val scanner = BarcodeScanning.getClient()
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        handleQrCodeDetected(barcode.rawValue ?: "")
                        break // Process only first QR code found
                    }
                    if (barcodes.isEmpty()) {
                        Toast.makeText(this, "No QR code found in image", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to scan QR code from image", Toast.LENGTH_SHORT).show()
                }
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showMyQrCode() {
        // Generate user's UPI QR code
        val upiId = "user@paytm" // Replace with actual user UPI ID
        val name = "User Name" // Replace with actual user name
        val upiString = "upi://pay?pa=$upiId&pn=$name"

        try {
            val bitmap = generateQrCode(upiString)
            showQrCodeDialog(bitmap, "My UPI QR Code")
        } catch (e: WriterException) {
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateQrCode(text: String): Bitmap {
        val writer = MultiFormatWriter()
        val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bitmap
    }

    private fun showQrCodeDialog(bitmap: Bitmap, title: String) {
        val imageView = ImageView(this)
        imageView.setImageBitmap(bitmap)
        imageView.setPadding(50, 50, 50, 50)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(imageView)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openContactPicker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {
            contactLauncher.launch(null)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                CONTACT_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun handleContactSelection(uri: Uri) {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                val name = if (nameIndex >= 0) it.getString(nameIndex) else ""
                val number = if (numberIndex >= 0) it.getString(numberIndex) else ""

                inputMobileOrName.setText(if (number.isNotEmpty()) number else name)
            }
        }
    }

    private fun handleQrCodeDetected(qrCode: String) {
        if (qrCode.startsWith("upi://")) {
            // Handle UPI QR code
            Toast.makeText(this, "UPI QR Code detected: $qrCode", Toast.LENGTH_LONG).show()
            // Here you can parse the UPI string and proceed with payment
            processUpiPayment(qrCode)
        } else {
            Toast.makeText(this, "QR Code: $qrCode", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processUpiPayment(upiString: String) {
        // Parse UPI string and extract payment details
        // Example: upi://pay?pa=merchant@upi&pn=MerchantName&am=100&cu=INR

        val uri = Uri.parse(upiString)
        val payeeAddress = uri.getQueryParameter("pa")
        val payeeName = uri.getQueryParameter("pn")
        val amount = uri.getQueryParameter("am")
        val currency = uri.getQueryParameter("cu")

        // Show payment confirmation dialog or navigate to payment screen
        showPaymentDialog(payeeAddress, payeeName, amount, currency)
    }

    private fun showPaymentDialog(payeeAddress: String?, payeeName: String?, amount: String?, currency: String?) {
        val message = buildString {
            append("Payment Details:\n")
            append("Payee: ${payeeName ?: "Unknown"}\n")
            append("UPI ID: ${payeeAddress ?: "Unknown"}\n")
            append("Amount: ${amount ?: "Not specified"} ${currency ?: ""}")
        }

        AlertDialog.Builder(this)
            .setTitle("Confirm Payment")
            .setMessage(message)
            .setPositiveButton("Pay") { _, _ ->
                // Proceed with payment
                Toast.makeText(this, "Proceeding with payment...", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    // QR Code Analyzer class
    private class QrCodeAnalyzer(private val onQrCodeDetected: (String) -> Unit) : ImageAnalysis.Analyzer {
        private val scanner = BarcodeScanning.getClient()

        @OptIn(ExperimentalGetImage::class)
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            barcode.rawValue?.let { onQrCodeDetected(it) }
                        }
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val CONTACT_PERMISSION_REQUEST_CODE = 1001
    }
}