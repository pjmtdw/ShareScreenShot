package karuta.hpnpwd.share_screen_shot

import _root_.android.os.Bundle
import _root_.android.app.Activity
import _root_.android.text.format.Time
import _root_.android.net.Uri
import _root_.android.content.Intent
import _root_.java.io.DataOutputStream

class MainActivity extends Activity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val process = Runtime.getRuntime().exec("su")
    val os = new DataOutputStream(process.getOutputStream())
    val time = new Time()
    time.setToNow
    val times = time.format("%Y%m%d-%H%M%S")
    val path = s"/mnt/sdcard/Pictures/Screenshots/scr-${times}.png"
    os.writeBytes(s"/system/bin/screencap -p ${path}\n")
    os.writeBytes("exit\n")
    os.flush
    os.close
    process.waitFor
    val uri = Uri.parse("file://"+path)
    val pm = getPackageManager
    val intent = new Intent
    intent.setAction(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM,uri)
    intent.setType("image/png")
    startActivity(Intent.createChooser(intent,"choose"))
  }
}
