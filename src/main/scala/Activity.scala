package karuta.hpnpwd.share_screen_shot

import _root_.android.os.Bundle
import _root_.android.support.v7.app.ActionBarActivity
import _root_.android.text.format.Time
import _root_.java.io.DataOutputStream

class MainActivity extends ActionBarActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    moveTaskToBack(true)
    val process = Runtime.getRuntime().exec("su")
    val os = new DataOutputStream(process.getOutputStream())
    val time = new Time()
    time.setToNow
    val times = time.format("%Y%m%d-%H%M%S")
    os.writeBytes(s"/system/bin/screencap -p /mnt/sdcard/Pictures/Screenshots/scr-${times}.png\n")
    os.writeBytes("exit\n")
    os.flush
    os.close
    process.waitFor
    finish
  }
}
