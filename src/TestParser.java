import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class TestParser {

	@Test
	public void testParser() {
		String todayDate = getToday();
		// test importance
		// boundary and equivalence partitionining
		// higher success
		testParseInput("test for parsing importance",
				"add hello 12/03/2015 01:00 null null 3 null null ",
				".a 12th mar 1am .i 3 hello");
		// lower success
		testParseInput("test for parsing importance",
				"add hello 12/03/2015 01:00 null null 0 null null ",
				".a 12th mar 1am .i 0 hello");
		// higher failure
		testParseInput(
				"test for parsing importance",
				"error .i 4 hello 12/03/2015 01:00 null null null invalid importance level null ",
				".a 12th mar 1am .i 4 hello");
		// lower failure
		testParseInput(
				"test for parsing importance",
				"error .i -1 hello 12/03/2015 01:00 null null null invalid importance level null ",
				".a 12th mar 1am .i -1 hello");

		// Test for delete range
		// Correct cases
		testParseInput("test for delete range",
				"delete null null null null null null null 1 2 3 4 5 7 9 ",
				".d 1- 5 7 9");

		testParseInput("test for delete range",
				"delete null null null null null null null 1 2 3 4 5 7 9 ",
				".d 1 -5 7 9");
		testParseInput("test for delete range",
				"delete null null null null null null null 1 2 3 4 5 7 9 ",
				".d 1 - 5 7 9");
		// failure cases
		testParseInput(
				"test for delete range",
				"error hello null null null null null Invalid parameter 1 5 7 9 ",
				".d 1 - hello 5 7 9");
		testParseInput(
				"test for delete range",
				"error hello null null null null null Invalid parameter 1 5 7 9 ",
				".d 1 hello- 5 7 9");

		// time range
		// time range with space(correct cases)
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello by at to  1 pm to 2pm world 12th mar 15");
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello by at to  1 pm - 2pm world 12th mar 15");
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello  by at to  1pm - 2pm world 12th mar ");

		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello by at to 1pm - 2 pm world 12th mar ");
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello  by at to 1 pm - 2 pm world 12th mar ");
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello by at to 1 pm - 2 pm world 12th mar ");
		// time range with space
		testParseInput(
				"test for time range",
				"add hello - 2 world 12/03/2015 13:00 null null null null null ",
				"hello by at to 1 pm - 2 world 12th mar ");
		testParseInput(
				"test for time range",
				"add hello by at to 1 - world 12/03/2015 14:00 null null null null null ",
				"hello by at to 1  - 2 pm world 12th mar ");
		testParseInput(
				"test for time range",
				"add hello 1 world 12/03/2015 13:00 14:00 null null null null ",
				"hello  by at to 1 1 pm - 2 pm world 12th mar ");
		// failure cases
		testParseInput(
				"test for time range",
				"error hello 3pm - 2pm world 12/03/2015 14:00 null null null Time range is invalid. null ",
				"hello 3pm - 2pm world 12th mar 15");
		testParseInput(
				"test for time range",
				"error hello 3pm-2pm world 12/03/2015 null null null null Time range is invalid. null ",
				"hello 3pm-2pm world 12th mar 15");
		testParseInput(
				"test for time range",
				"error hello 3.61pm-2pm world 12/03/2015 null null null null Invalid time found. null ",
				"hello 3.61pm-2pm world 12th mar 15");
		testParseInput(
				"test for time range",
				"error hello 3.61pm - 2pm world 12/03/2015 14:00 null null null Invalid time found. null ",
				"hello 3.61pm - 2pm world 12th mar 15");
		testParseInput(
				"test for time range",
				"error hello 3pm-2.61pm world 12/03/2015 null null null null Invalid time found. null ",
				"hello 3pm-2.61pm world 12th mar 15");

		// test for no time
		testParseInput("test for no time",
				"add hello 12/03/2015 no time null null null null null ",
				"hello 12th mar by no time");
		testParseInput("test for no time",
				"add hello 10pm 12/03/2015 no time null null null null null ",
				"hello 12th mar 10pm .nt");

		// test for double time for time range
		testParseInput("test for time range",
				"add hello pm 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar 1pm-2pm pm");
		testParseInput(
				"test for time range(double time)",
				"add hello 10pm 12-12 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar 10pm 1pm-2pm 12-12");

		// testing time range basic
		// correct cases
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello by at to  1 pm-2pm world 12th mar 15");
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello  by at to  1pm-2pm world 12th mar ");

		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello by at to 1pm-2 pm world 12th mar ");
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello  by at to 1 pm-2 pm world 12th mar ");
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello by at to 1 pm-2 pm world 12th mar ");

		testParseInput(
				"test for time range",
				"add hello pm-2 pm world 12/03/2015 null null null null null null ",
				"hello pm-2 pm world 12th mar ");
		// mixed time range
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello 13:00-2 pm world 12th mar ");
		testParseInput("test for time range",
				"add hello world 12/03/2015 13:00 14:00 null null null null ",
				"hello 1.00 pm-14:00 world 12th mar ");

		// testing for details
		testParseInput("test for details empty", "add hello " + todayDate
				+ " 13:00 null  null null null ", "hello 1pm .dtl ");
		testParseInput("test for details empty", "add hello " + todayDate
				+ " null null  null null null ", "hello .dtl ");
		testParseInput("test for no details(null)", "add hello " + todayDate
				+ " null null null null null null ", "hello ");

		// test for delete date
		testParseInput(
				"test for delete date",
				"error maybe delete hello null null null null null No Date is found. null ",
				"maybe delete date delete hello");
		testParseInput(
				"test for delete date",
				"error maybe delete hello 25/01/2015 null null null null There are extra attributes. Pls remove them. null ",
				"maybe delete date delete hello 25 jan 15");
		testParseInput("test for delete date",
				"delete date null 25/01/2014 null null null null null null ",
				"25th jan 14 delete date.");
		testParseInput("test for delete date",
				"delete date null 25/01/2014 null null null null null null ",
				"delete date 25th jan 14 .");
		// test for show today
		testParseInput("test for special commands", "search null " + todayDate
				+ " null null null null null null ", "show today");

		// test for special commands
		testParseInput("test for special commands",
				"show this week null null null null null null null null ",
				"show this week");
		testParseInput("test for special commands",
				"show week null null null null null null null null ", ".sw");
		testParseInput("test for special commands", "add clear archive hello "
				+ todayDate + " null null null null null null ",
				"add clear archive hello");
		testParseInput(
				"test for special commands",
				"error null null null null null null This is a special command. You cannot input extra attributes null ",
				"clear archive hello");
		testParseInput("test for special commands",
				"clear archive null null null null null null null null ",
				"clear archive");
		testParseInput("test for special commands",
				"show all null null null null null null null null ", "show all");
		testParseInput("test for special commands",
				"sort importance null null null null null null null null ",
				"sort impt");
		testParseInput("test for special commands",
				"sort time null null null null null null null null ", "sort");
		testParseInput(
				"test for special commands",
				"error null null null null null null This is a special command. You cannot input extra attributes null ",
				"sort hello");
		testParseInput("test for special commands",
				"sort alpha null null null null null null null null ", ".sap");
		testParseInput("test for special commands",
				"sort time null null null null null null null null ", ".st");
		testParseInput("test for special commands",
				"delete all null null null null null null null null ",
				"delete all");
		testParseInput("test for special commands",
				"show details null null null null null null null null ", ".sd");
		testParseInput("test for special commands",
				"hide details null null null null null null null null ", ".hd");
		testParseInput("test for special commands",
				"view archive null null null null null null null null ",
				"view archive");
		testParseInput("test for special commands",
				"undo null null null null null null null null ", "undo");
		testParseInput("test for special commands",
				"redo null null null null null null null null ", "redo");
		testParseInput("test for special commands",
				"show floating null null null null null null null null ",
				"show floating");
		testParseInput("test for special commands",
				"exit null null null null null null null null ", "exit");
		testParseInput("test for special commands",
				"delete past null null null null null null null null ",
				"delete past");
		testParseInput("test for special commands",
				"clear null null null null null null null null ", ".c");
		testParseInput("test for special commands",
				"delete today null null null null null null null null ",
				"delete today");

		// test for escape sequence
		testParseInput("test for slash for details and task processor",
				"add hello hello hello. " + todayDate
						+ " null null /world is beautiful null null null ",
				"hello /hello hello. .dtl /world is beautiful");

		testParseInput("test for full stop", "error null " + todayDate
				+ " null null null null No Task name is found. null ", "");

		testParseInput("test for full stop", "add .... " + todayDate
				+ " null null null null null null ", " ....");
		// tests for full-stop
		testParseInput("test for full stop",
				"add hello. 11/11/2014 15:00 null null 2 null null ",
				" impt 2. hello is on 11-11-2014. on 15:00.");

		testParseInput("test for full stop", "add hello. ... " + todayDate
				+ " 23:59 null null null null null ",
				" hello. .... at 11.59 pm.");
		testParseInput(
				"test for parsing importance",
				"add howmework is important hello 12/03/2015 01:00 null null 2 null null ",
				".a 12th mar 1am howmework is important important 2 hello");

		testParseInput("test for spelled time format one",
				"add hello 12/03/2015 01:00 null null null null null ",
				".a 12 mar 1am hello");
		testParseInput("test for spelled time format three",
				"add hello 12/03/2015 01:00 null null null null null ",
				".a 12 mar 01:00am hello");
		testParseInput("test for spelled time format two",
				"add hello 12/03/2015 01:00 null null null null null ",
				".a 12 mar 01:00 hello");
		testParseInput("test for spelled time format three",
				"add hello 12/03/2015 01:00 null null null null null ",
				".a 12 mar 01.00am hello");
		testParseInput("test for spelled time format three",
				"add hello 12/03/2015 01:00 null null null null null ",
				".a 12 mar 01.00 am hello");
		testParseInput("test for spelled time format three",
				"add hello 12/03/2015 01:00 null null null null null ",
				".a 12 mar 1 am hello");
		testParseInput("test for spelled time format three",
				"add hello 12/03/2015 01:00 null null null null null ",
				".a 12 mar on at 1.00 am hello");

		testParseInput("test for delete",
				"delete null null null null null null null 1 4 5 8 1 ",
				"1 4 5 8 1 .d.");

		// testing for empty spaces
		testParseInput("test for extra empty spaces",
				"add mon hello. ft null null null null null null ",
				".a          mon          hello ft              .");

		testParseInput("test for full-stop",
				"add it. 12/03/2015 null null null 2 null null ",
				"it on 12th mar. is of impt 2. ");

	
		//test for today
		testParseInput("test for tmr", "add hello " + todayDate
				+ " null null null null null null ", "hello tdy");
		testParseInput("test for today", "add hello " + todayDate
				+ " null null go home null null null ",
				"hello today .dtl go home");
		testParseInput("test for possible error from parameters", "add 3 " + todayDate
				+ " null null null null null null ", "3");

		// testing for edit
		testParseInput(
				"test for edit",
				"edit the world on 24rd mar 24/04/2015 17:00 null hello .dtl null null 1 ",
				"the world on 24rd mar is at 5pm 1 at 24th apr to be edited .dtl hello .dtl ");
		testParseInput(
				"test for edit",
				"edit the world on 23rd mar null 17:00 null hello .dtl null null 1 ",
				"the world  1 on /23rd mar at 5pm to be edited .dtl hello .dtl ");
		testParseInput("test for edit",
				"edit null null null null hello .dtl null null 1 ",
				" edit 1 .dtl hello .dtl ");
		testParseInput("test for edit",
				"edit null 24/04/2015 null null null null null 1 ",
				" edit for 1 24th apr ");
		testParseInput("test for edit",
				"edit null null 14:00 null null null null 1 ",
				" edit for 1 14:00 ");
		testParseInput("test for edit",
				"edit null null 23:00 null null null null 1 ",
				" edit for 1 11pm ");
		testParseInput("test for edit",
				"edit null null 12:15 null null null null 1 ",
				" edit for 1 12.15pm ");
		testParseInput("test for edit",
				"edit hello cat. null null null null null null 1 ",
				" edit 1 hello cat.");
		testParseInput("test for edit",
				"edit null null null null null 3 null 1 ",
				" edit importance 3 1");
		testParseInput("test for edit",
				"edit null null null null null 3 null 1 ", " edit 1 impt 3 ");
		testParseInput("test for edit",
				"edit null null null null null 3 null 1 ", " edit 1 .i 3");
		testParseInput("test for edit", "edit 2 null null null null 3 null 1 ",
				" edit 1 2 .i 3");
		//edit failure case
		testParseInput(
				"test for edit",
				"error null null null null null null No Parameters is found. null ",
				"edit");

		// test add floating task
		testParseInput("test for today",
				"add mon hello. ft null null null null null null ",
				".a mon hello ft.");
		// test for edit
		testParseInput("test for edit",
				"edit hello 12/03/2012 12:00 null hello world 2 null 1 ",
				".e 1 12th mar 12 12:00 .i 2 hello .dtl hello world");
		testParseInput("test for edit",
				"edit null 12/03/2015 12:00 null null null null 1 ",
				".e 1 12th mar 12:00");
		// failure case for edit with no parameters
		testParseInput(
				"test for command",
				"error add make breakfast at null null null Just get rid. null No Parameters is found. null ",
				"edit the add make breakfast at .dtl Just get rid.");

		// test for auto add with only date, time,details and importance
		testParseInput(
				"test for command",
				"add make breakfast 14/04/2015 13:00 null Just get rid. 3 null null ",
				"make breakfast .i 3 at 13:00 on 14/4/15 .dtl Just get rid.");
		testParseInput(
				"test for command",
				"add make breakfast 14/04/2015 null null Just get rid. null null null ",
				"make breakfast on 14th apr .dtl Just get rid.");
		testParseInput(
				"test for command",
				"add make breakfast 14/04/2015 null null Just get rid. null null null ",
				"make breakfast on 14th apr 15 .dtl Just get rid.");
		testParseInput(
				"test for command",
				"add make breakfast 14/04/2015 null null Just get rid. null null null ",
				"make breakfast on 14th apr 15 to be added .dtl Just get rid.");
		testParseInput("test for command", "add make breakfast " + todayDate
				+ " null null Just get rid. null null null ",
				"make breakfast at to be added .dtl Just get rid.");

		testParseInput(
				"test for command",
				"error make breakfast at null null null Just get rid. null No Parameters is found. null ",
				"edit make breakfast at .dtl Just get rid.");

		// test for special command where it is not the first part of string
		testParseInput("test for clear all archive",
				"add I just want to clear all archive " + todayDate
						+ " null null Just get rid. null null null ",
				"I just want to clear all archive .dtl Just get rid.");
		// edit for edit
		testParseInput("test for edit",
				"edit null 12/03/2015 12:00 null null null null 1 ",
				".e 1 12th mar 12:00");
		testParseInput("test for edit",
				"edit hello 12/03/2015 12:00 null hello world 2 null 1 ",
				".e 1 12th mar 12:00 .i 2 hello .dtl hello world");
		testParseInput("test for edit",
				"edit hello 12/03/2015 null null hello world 2 null 1 ",
				".e 1 12th mar .i 2 hello .dtl hello world");
		
		// test edit with all other attributes other than date
		// to make sure that date is not added automatically
		testParseInput("test for edit",
				"edit hello null 12:00 null hello world 2 null 1 ",
				".e 1 12:00 .i 2 hello .dtl hello world");

		// test delete
		testParseInput("test for delete",
				"delete null null null null null null null 1 2 3 5 6 7 8 ",
				".d 1 2 3 5 6 7 8");
		testParseInput("test for delete",
				"delete null null null null null null null 1 ", ".d 1");
		testParseInput(
				"test for delete",
				"error hello null null null null null No Parameters is found. null ",
				".d hello");
		testParseInput(
				"test for delete",
				"error hello null null null null null There are extra attributes. Pls remove them. 1 2 1 ",
				"1 2 hello 1 .d.");

		// test details
		testParseInput(
				"test for parsing details",
				"add hello 12/03/2015 01:00 null everything is awesome .dtl the world is round. 3 null null ",
				".a 12th mar 1am .i 3 hello .dtl everything is awesome .dtl the world is round.");
		testParseInput("test for parsing details",
				"add hello 12/03/2015 01:00 null  3 null null ",
				".a 12th mar 1am .i 3 hello .dtl    ");

		// test timing
		testParseInput("test for time",
				"add hello 12/03/2015 19:00 null null null null null ",
				".a 12th mar 19:00 hello");
		testParseInput("test for today timing", "add hello " + todayDate
				+ " 23:59 null null null null null ", ".a 23:59 hello");
		testParseInput("test for no time",
				"add hello 12/03/2015 null null null null null null ",
				".a 12th mar hello");
		testParseInput("test for time",
				"add hello 12/03/2015 00:00 null null null null null ",
				".a 12th mar 12am hello");
		// failure cases for time
		testParseInput(
				"test for time",
				"error 24pm hello 12/03/2015 null null null null invalid time null ",
				".a 12th mar 24pm hello");
		testParseInput(
				"test for time",
				"error 13:00am hello 12/03/2015 null null null null invalid time null ",
				".a 12th mar 13:00am hello");
		testParseInput(
				"test for time",
				"error 24:59 hello 12/03/2015 null null null null invalid time null ",
				".a 12th mar 24:59 hello");
		testParseInput(
				"test for time",
				"error 12:60 hello 12/03/2015 null null null null invalid time null ",
				".a 12th mar 12:60 hello");

		// tests for spelled date two format
		testParseInput("test for spelled date format one",
				"add hello 12/03/2015 null null null null null null ",
				".a 12th mar hello");
		testParseInput("test for spelled date format one",
				"add hello 12/03/2015 null null null null null null ",
				".a 12 mar hello");
		testParseInput("test for spelled date format one",
				"add hello 01/11/2015 null null null null null null ",
				".a 1st nov hello");

		// tests for spelled date one format
		testParseInput("test for spelled date format one",
				"add hello 12/03/2015 null null null null null null ",
				".a 12th mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 01/01/2015 null null null null null null ",
				".a 1st jan 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello world 01/01/2015 null null null null null null ",
				".a 1st january 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello 01/03/2015 null null null null null null ",
				".a 1st mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 02/03/2015 null null null null null null ",
				".a 2nd mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 03/03/2015 null null null null null null ",
				".a 3rd mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 04/03/2015 null null null null null null ",
				".a 4th mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 11/03/2015 null null null null null null ",
				".a 11th mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 12/03/2015 null null null null null null ",
				".a 12th mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 13/03/2015 null null null null null null ",
				".a 13th mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 30/03/2015 null null null null null null ",
				".a 30th mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 24/03/2015 null null null null null null ",
				".a 24th mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 22/03/2015 null null null null null null ",
				".a 22nd mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 23/03/2015 null null null null null null ",
				".a 23rd mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 30/03/2015 null null null null null null ",
				".a 30th mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 31/03/2015 null null null null null null ",
				".a 31st mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 31/03/2014 null null null null null null ",
				".a 31st mar 2014 hello");
		testParseInput("test for spelled date format one",
				"add hello 31/03/2015 null null null null null null ",
				".a 31st mar 15 hello");
		testParseInput("test for spelled date format one",
				"add hello 31/03/2014 null null null null null null ",
				".a 31st mar 14 hello");
		testParseInput("test for spelled date format one",
				"add hello 29/02/2016 null null null null null null ",
				".a 29th february 16 hello");
		//test for spelled date format without suffix
		testParseInput("test for spelled date format one",
				"add hello 01/03/2015 null null null null null null ",
				".a 1 mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 0 mar 2015 hello null null null null null invalid Date null ",
				".a 0 mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 32 mar 2015 hello null null null null null invalid Date null ",
				".a 32 mar 2015 hello");

		// tests for format one
		testParseInput("test for date format one",
				"add hello 29/02/2012 null null null null null null ",
				".a 29/2/12 hello");
		testParseInput("test for date format one",
				"add hello 12/05/2015 null null null null null null ",
				".a 12/05/15 hello");
		testParseInput("test for date format one",
				"add hello 01/01/2015 null null null null null null ",
				".a 1/1/15 hello");
		// leap year test for date format one
		testParseInput("test for date format one",
				"add hello 29/02/2016 null null null null null null ",
				".a 29/2/16 hello");
		// error cases for date format one
		testParseInput(
				"test for date format one",
				"error 29/2/15 hello null null null null null invalid Date null ",
				".a 29/2/15 hello");

		// failure cases for date format one
		testParseInput("test for date format one",
				"add hello 12/04/2014 null null null null null null ",
				".a 12/04/14 hello");
		testParseInput("test for date format one", "add 12/05/ hello "
				+ todayDate + " null null null null null null ",
				".a 12/05/ hello");
		testParseInput("test for date format one", "add 12/05/2 hello "
				+ todayDate + " null null null null null null ",
				".a 12/05/2 hello");
		testParseInput("test for date format one", "add 12/05/123 hello "
				+ todayDate + " null null null null null null ",
				".a 12/05/123 hello");
	}

	private String getToday() {
		SimpleDateFormat formatToday = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		return formatToday.format(today);
	}

	private void testParseInput(String description, String expected,
			String command) {
		assertEquals(description, expected,
				TestDriverParser.getParsedInput(command));
	}

}
