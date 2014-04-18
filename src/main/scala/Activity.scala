package karuta.hpnpwd.share_screen_shot

import _root_.android.os.Bundle
import _root_.android.app.{Activity,Notification,NotificationManager,PendingIntent}
import _root_.android.text.format.Time
import _root_.android.net.Uri
import _root_.android.content.{Intent,ComponentName,Context}
import _root_.java.io.DataOutputStream

class MainActivity extends Activity {
  val NOTIFY_ID = 1
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    // show notification
    val app_name = getString(R.string.app_name)
    val notification = new Notification(android.R.drawable.star_on, app_name, System.currentTimeMillis)
    notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT
    val itnt = new Intent(this, classOf[MainActivity])
    notification.setLatestEventInfo(this, app_name, app_name, PendingIntent.getActivity(this, 1, itnt, 0))
    val notifier = getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    notifier.notify(NOTIFY_ID, notification)

    // take screenshot
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

    // send to Skitch
    val uri = Uri.parse("file://"+path)
    val pm = getPackageManager
    val intent = new Intent
    intent.setAction(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM,uri)
    intent.setType("image/png")
    // startActivity(Intent.createChooser(intent,"choose"))
    intent.setComponent(new ComponentName("com.evernote.skitch","com.evernote.skitch.app.CanvasActivity"))

    notifier.cancel(NOTIFY_ID)
    
    startActivity(intent)
  }
}
