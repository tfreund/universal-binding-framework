package de.freund.syncer.transformer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToStringTransformer {
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

	public String dateToString(Date date) {
		return simpleDateFormat.format(date);
	}

	public Date stringToDate(String dateString) {
		try {
			return simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
