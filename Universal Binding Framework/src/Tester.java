import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTextField;

import de.freund.syncer.BOCapable;
import de.freund.syncer.Syncer;
import de.freund.syncer.UISyncTransaction;

public class Tester {

	private static JTextField tf;
	private static JLabel errorLabel;

	public static void main(String[] args) {

		Person person = new Person();
		person.setBirthday(new Date());

		tf = new JTextField();
		errorLabel = new JLabel();

		Syncer<Person> syncer = new Syncer<Person>(Tester::bindValues);
		syncer.setObject(person);

		System.out.println(tf.getText());
		
		tf.setText("failure");
		
		syncer.writeUiToBO();
		System.out.println("Label: " + errorLabel.getText());
	}

	public static void bindValues(Syncer<Person> syncer) throws Exception {
		DateTransformer dt = new DateTransformer();

		BOCapable syncTransaction = new UISyncTransaction<Date, String>(syncer.getObject()::getBirthday, syncer.getObject()::setBirthday, tf::getText, tf::setText,
				dt::dateToString, dt::stringToDate, (e) -> errorLabel.setText(e.getMessage()));

		syncer.addSyncTransaction(syncTransaction);
	}

}
