
//
// ActionAdapter.java
//
package java.awt.event;   
import java.awt.event.*;

public abstract class ActionAdapter extends Object implements ActionListener {
    public ActionAdapter() {
    }


    public abstract void actionPerformed(ActionEvent ae);
}
