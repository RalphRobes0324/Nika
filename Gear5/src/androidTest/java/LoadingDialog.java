import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import ca.nika.it.gear5.R;

public class LoadingDialog {

    AlertDialog dialog;
    Activity activity;

    LoadingDialog(Activity myActivity){
        activity=myActivity;
    }

    private void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.load_dialog,null));
        builder.setCancelable(true);

        dialog=builder.create();
        dialog.show();
    }

    private void dismissLoading(){
        dialog.dismiss();
    }
}
