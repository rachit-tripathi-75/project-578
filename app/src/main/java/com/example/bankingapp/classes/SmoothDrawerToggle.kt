import android.animation.ValueAnimator
import android.util.Log
import android.view.animation.DecelerateInterpolator
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class SmoothDrawerToggle(
    private val drawerLayout: DrawerLayout,
    private val duration: Long = 300
) {

    fun openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Get the drawer view by finding the drawer directly from the DrawerLayout
            val drawerView = drawerLayout.getChildAt(1)

            // Check if the drawer view exists
            if (drawerView != null) {
                // Measure drawer width
                val width = drawerView.width

                // Create animator
                val animator = ValueAnimator.ofFloat(0f, 1f)
                animator.duration = duration
                animator.interpolator = DecelerateInterpolator()

                // Update drawer position
                animator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Float
                    drawerView.translationX = (value - 1) * width
                }

                // Start animation
                animator.start()

                // Set drawer state
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START)
                drawerLayout.openDrawer(GravityCompat.START)
            } else {
                Log.e("SmoothDrawerToggle", "Drawer view not found.")
            }
        }
    }

    fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Get the drawer view by finding the drawer directly from the DrawerLayout
            val drawerView = drawerLayout.getChildAt(1)

            // Check if the drawer view exists
            if (drawerView != null) {
                // Measure drawer width
                val width = drawerView.width

                // Create animator
                val animator = ValueAnimator.ofFloat(1f, 0f)
                animator.duration = duration
                animator.interpolator = DecelerateInterpolator()

                // Update drawer position
                animator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Float
                    drawerView.translationX = (value - 1) * width
                }

                // Start animation
                animator.start()

                // Set drawer state
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                Log.e("SmoothDrawerToggle", "Drawer view not found.")
            }
        }
    }
}
