import static org.junit.Assert.*;

import org.junit.Test;

public class TestParser {

	@Test
	public void testParser() {
	/*	testParseInput("test for delete date",
				"error maybe delete hello null null null null No Date is found. null ",
				"1st mar 2015 hello world");
		*/
		//to delete away
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar 1 pm-2pm");	
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar  1pm-2pm");	
	
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar to 1pm-2 pm");	
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar   1 pm-2 pm");
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar 1 pm-2 pm");
		
		  
	//testing time range 
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar by at to  1 pm-2pm");	
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar by at to  1pm-2pm");	
	
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar by at to 1pm-2 pm");	
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar  by at to 1 pm-2 pm");
		testParseInput(
				"test for time range",
				"add hello 12/03/2015 13:00 14:00 null null null null ",
				"hello 12th mar by at to 1 pm-2 pm");
		
		
		
		
		//testing for details
		
		testParseInput(
				"test for details empty",
				"add hello 04/11/2014 13:00 null  null null null ",
				"hello 1pm .dtl ");
		testParseInput(
				"test for details empty",
				"add hello 04/11/2014 null null  null null null ",
				"hello .dtl ");
		testParseInput(
				"test for no details(null)",
				"add hello 04/11/2014 null null null null null null ",
				"hello ");
	
		//test for delete date
		testParseInput("test for delete date",
				"error maybe delete hello null null null null null No Date is found. null ",
				"maybe delete date delete hello");
		testParseInput("test for delete date",
				"error maybe delete hello 25/01/2015 null null null null There are extra attributes. Pls remove them. null ",
				"maybe delete date delete hello 25 jan 15");
		testParseInput("test for delete date",
				"delete date null 25/01/2014 null null null null null null ",
				"25th jan 14 delete date.");
		testParseInput("test for delete date",
				"delete date null 25/01/2014 null null null null null null ",
				"delete date 25th jan 14 .");
		//test for show today
		testParseInput("test for special commands",
				"search null 04/11/2014 null null null null null null ",
				"show today");
		
		
		//test for special commands
		testParseInput("test for special commands",
				"show this week null null null null null null null null ",
				"show this week");
		testParseInput("test for special commands",
				"show week null null null null null null null null ",
				".sw");
		testParseInput("test for special commands",
				"add clear archive hello 04/11/2014 null null null null null null ",
				"add clear archive hello");
		testParseInput("test for special commands",
				"error null null null null null null This is a special command. You cannot input extra attributes null ",
				"clear archive hello");
		testParseInput("test for special commands",
				"clear archive null null null null null null null null ",
				"clear archive");
		testParseInput("test for special commands",
				"show all null null null null null null null null ",
				"show all");
	
		testParseInput("test for special commands",
				"sort importance null null null null null null null null ",
				"sort impt");
		testParseInput("test for special commands",
				"sort time null null null null null null null null ",
				"sort");
		testParseInput("test for special commands",
				"error null null null null null null This is a special command. You cannot input extra attributes null ",
				"sort hello");
		testParseInput("test for special commands",
				"sort alpha null null null null null null null null ",
				".sap");
		testParseInput("test for special commands",
				"sort time null null null null null null null null ",
				".st");
		testParseInput("test for special commands",
				"delete all null null null null null null null null ",
				"delete all");
		testParseInput("test for special commands",
				"show details null null null null null null null null ",
				".sd");
		testParseInput("test for special commands",
				"hide details null null null null null null null null ",
				".hd");
		testParseInput("test for special commands",
				"view archive null null null null null null null null ",
				"view archive");
		

		testParseInput("test for special commands",
				"undo null null null null null null null null ",
				"undo");
		testParseInput("test for special commands",
				"redo null null null null null null null null ",
				"redo");
		testParseInput("test for special commands",
				"show floating null null null null null null null null ",
				"show floating");
		testParseInput("test for special commands",
				"exit null null null null null null null null ",
				"exit");
		testParseInput("test for special commands",
				"delete past null null null null null null null null ",
				"delete past");
		testParseInput("test for special commands",
				"clear null null null null null null null null ",
				".c");
		testParseInput("test for special commands",
				"delete today null null null null null null null null ",
				"delete today");
		
		
		
		
		
		
		
		
		testParseInput("test for slash for details and task processor",
				"add hello hello hello. 04/11/2014 null null /world is beautiful null null null ",
				"hello /hello hello. .dtl /world is beautiful");
 
		testParseInput("test for full stop",
				"error null 04/11/2014 null null null null No Task name is found. null ",
				"");
		
		testParseInput("test for full stop",
				"add .... 04/11/2014 null null null null null null ",
				" ....");
		//tests for full-stop
		testParseInput("test for full stop",
				"add hello. 11/11/2014 15:00 null null 2 null null ",
				" impt 2. hello is on tues. on 15:00.");

		testParseInput("test for full stop",
				"add hello. ... 04/11/2014 23:59 null null null null null ",
				" hello. .... at 11.59 pm.");
		testParseInput(
				"test for parsing importance",
				"add howmework is important hello 12/03/2015 01:00 null null 2 null null ",
				".a 12th mar 1am howmework is important important 2 hello");
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

		testParseInput("test for tmr",
				"add it. 12/03/2015 null null null 2 null null ",
				"it on 12th mar. is of impt 2. ");

		// testParseInput("test for tmr",
		// "add hello 16102014 null null null null null ",
		// "showall .dtl hello");

		testParseInput("test for tmr",
				"add hello 04/11/2014 null null null null null null ", "hello tdy");
		testParseInput("test for today",
				"add hello 04/11/2014 null null go home null null null ",
				"hello today .dtl go home");
		testParseInput("test for tmr",
				"add hello 05/11/2014 null null null null null null ",
				"hello tomorrow");
		testParseInput("test for tmr",
				"add hello 05/11/2014 null null null null null null ", "hello tmr");
		testParseInput("test for add",
				"add 3 04/11/2014 null null null null null null ", "3");

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
				"error null null null null null null No Parameters is found. null ",
				"edit");
		testParseInput("test for edit",
				"edit null null null null hello .dtl null null 1 ",
				" edit 1 .dtl hello .dtl ");
		testParseInput("test for edit",
				"edit null 24/04/2015 null null null null null 1 ",
				" edit for 1 24th apr ");
		testParseInput("test for edit",
				"edit null null 14:00 null null null null 1 ", " edit for 1 14:00 ");
		testParseInput("test for edit",
				"edit null null 23:00 null null null null 1 ", " edit for 1 11pm ");
		testParseInput("test for edit",
				"edit null null 12:15 null null null null 1 ",
				" edit for 1 12.15pm ");
		testParseInput("test for edit",
				"edit hello cat. null null null null null null 1 ",
				" edit 1 hello cat.");
		testParseInput("test for edit", "edit null null null null null 3 null 1 ",
				" edit importance 3 1");
		testParseInput("test for edit", "edit null null null null null 3 null 1 ",
				" edit 1 impt 3 ");
		testParseInput("test for edit", "edit null null null null null 3 null 1 ",
				" edit 1 .i 3");
		testParseInput("test for edit", "edit 2 null null null null 3 null 1 ",
				" edit 1 2 .i 3");

		// test add floating task
		testParseInput("test for today",
				"add mon hello. ft null null null null null null ",
				".a mon hello ft.");

		testParseInput(
				"test for edit",
				"edit hello 12/03/2012 12:00 null hello world 2 null 1 ",
				".e 1 12th mar 12 12:00 .i 2 hello .dtl hello world");
		testParseInput("test for edit",
				"edit null 12/03/2015 12:00 null null null null 1 ",
				".e 1 12th mar 12:00");
		testParseInput(
				"test for command",
				"error add make breakfast at null null null Just get rid. null No Parameters is found. null ",
				"edit the add make breakfast at .dtl Just get rid.");
		testParseInput(
				"test for command",
				"add make breakfast 14/04/2015 null null Just get rid. null null null ",
				"make breakfast on 14/4/15 .dtl Just get rid.");
		testParseInput(
				"test for command",
				"add make breakfast 14/04/2015 13:00 null Just get rid. null null null ",
				"make breakfast at 13:00 on 14/4/15 .dtl Just get rid.");
		testParseInput(
				"test for command",
				"add make breakfast 14/04/2015 13:00 null Just get rid. 3 null null ",
				"make breakfast .i 3 at 13:00 on 14/4/15 .dtl Just get rid.");
		// testParseInput("test for command",
		// "edit make breakfast null null  Just get rid .dtl null null null ",
		// "make 1 breakfast hello at to be edited .dtl Just get rid.");
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
		testParseInput(
				"test for command",
				"add make breakfast 04/11/2014 null null Just get rid. null null null ",
				"make breakfast at to be added .dtl Just get rid.");

		testParseInput(
				"test for command",
				"error make breakfast at null null null Just get rid. null No Parameters is found. null ",
				"edit make breakfast at .dtl Just get rid.");

		// ignore extra stuff inputs
		testParseInput(
				"test for clear all archive",
				"add I just want to clear all archive 04/11/2014 null null Just get rid. null null null ",
				"I just want to clear all archive .dtl Just get rid.");
		// edit
		// testParseInput("test for delete",
		// "delete null null null null null null 8 7 6 5 3 2 1 ",
		// ".e 1 12th mar");
		testParseInput("test for edit",
				"edit null 12/03/2015 12:00 null null null null 1 ",
				".e 1 12th mar 12:00");
		testParseInput("test for edit",
				"edit hello 12/03/2015 12:00 null hello world 2 null 1 ",
				".e 1 12th mar 12:00 .i 2 hello .dtl hello world");
		testParseInput("test for edit",
				"edit hello 12/03/2015 null null hello world 2 null 1 ",
				".e 1 12th mar .i 2 hello .dtl hello world");
		testParseInput("test for edit",
				"edit hello null 12:00 null hello world 2 null 1 ",
				".e 1 12:00 .i 2 hello .dtl hello world");

		// test delete
		testParseInput("test for delete",
				"delete null null null null null null null 1 2 3 5 6 7 8 ",
				".d 1 2 3 5 6 7 8");
		testParseInput("test for delete",
				"delete null null null null null null null 1 ", ".d 1");
		testParseInput("test for delete",
				"error hello null null null null null No Parameters is found. null ",
				".d hello");
		testParseInput("test for delete",
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
		testParseInput("test for today timing",
				"add hello 04/11/2014 23:59 null null null null null ",
				".a 23:59 hello");
		testParseInput("test for today timing",
				"add hello 04/11/2014 23:59 null null null null null ",
				".a 23:59 hello");
		testParseInput("test for time",
				"add hello 12/03/2015 null null null null null null ",
				".a 12th mar hello");
		testParseInput(
				"test for time",
				"error 24pm hello 12/03/2015 null null null null invalid time null ",
				".a 12th mar 24pm hello");
		testParseInput(
				"test for time",
				"error 13:00am hello 12/03/2015 null null null null invalid time null ",
				".a 12th mar 13:00am hello");
		testParseInput("test for time",
				"add hello 12/03/2015 00:00 null null null null null ",
				".a 12th mar 12am hello");
		testParseInput(
				"test for time",
				"error 12:60 hello 12/03/2015 null null null null invalid time null ",
				".a 12th mar 12:60 hello");
		testParseInput(
				"test for time",
				"error 24:59 hello 12/03/2015 null null null null invalid time null ",
				".a 12th mar 24:59 hello");
		testParseInput("test for today",
				"add hello 04/11/2014 null null null null null null ", ".a hello");

		// to test today

		// to test speed day so need to change the spelled day for testing
		testParseInput("test for spelled day",
				"add hello 10/11/2014 null null null null null null ",
				".a mon hello");
		testParseInput("test for spelled day",
				"add hello 10/11/2014 null null null null null null ",
				".a monday hello");
		testParseInput("test for spelled day",
				"add hello 11/11/2014 null null null null null null ",
				".a tues hello");
		testParseInput("test for spelled day",
				"add hello 11/11/2014 null null null null null null ",
				".a tuesday hello");
		testParseInput("test for spelled day",
				"add hello 05/11/2014 null null null null null null ",
				".a wed hello");
		testParseInput("test for spelled day",
				"add hello 05/11/2014 null null null null null null ",
				".a wednesday hello");
		testParseInput("test for spelled day",
				"add hello 06/11/2014 null null null null null null ",
				".a thurs hello");
		testParseInput("test for spelled day",
				"add hello 06/11/2014 null null null null null null ",
				".a thursday hello");
		testParseInput("test for spelled day",
				"add hello 07/11/2014 null null null null null null ",
				".a fri hello");
		testParseInput("test for spelled day",
				"add hello 07/11/2014 null null null null null null ",
				".a friday hello");
		testParseInput("test for spelled day",
				"add hello 08/11/2014 null null null null null null ",
				".a sat hello");
		testParseInput("test for spelled day",
				"add hello 08/11/2014 null null null null null null ",
				".a Saturday hello");
		testParseInput("test for spelled day",
				"add hello 09/11/2014 null null null null null null ",
				".a sun hello");
		testParseInput("test for spelled day",
				"add hello 09/11/2014 null null null null null null ",
				".a SUNDAY hello");
		// to test today's date so need to change to today's date before testing
		testParseInput("test for spelled date format one",
				"add hello 23/11/2014 null null null null null null ",
				".a 23rd nov hello");
		// tests for spelled date two format
		testParseInput("test for spelled date format one",
				"add hello 12/03/2015 null null null null null null ",
				".a 12th mar hello");
		testParseInput("test for spelled date format one",
				"add hello 12/03/2015 null null null null null null ",
				".a 12 mar hello");
		testParseInput("test for spelled date format one",
				"add hello 07/11/2014 null null null null null null ",
				".a 7 nov hello");

		// tests for spelled date one format
		testParseInput("test for spelled date format one",
				"add hello 12/03/2015 null null null null null null ",
				".a 12th mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 22th mar 2015 hello null null null null null invalid Date null ",
				".a 22th mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 23th mar 2015 hello null null null null null invalid Date null ",
				".a 23th mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 1th mar 2015 hello null null null null null invalid Date null ",
				".a 1th mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 2th mar 2015 hello null null null null null invalid Date null ",
				".a 2th mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 3th mar 2015 hello null null null null null invalid Date null ",
				".a 3th mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 12nd mar 2015 hello null null null null null invalid Date null ",
				".a 12nd mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 13rd mar 2015 hello null null null null null invalid Date null ",
				".a 13rd mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 11st mar 2015 hello null null null null null invalid Date null ",
				".a 11st mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 01/01/2015 null null null null null null ",
				".a 1st jan 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello world 01/01/2015 null null null null null null ",
				".a 1st january 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/02/2015 null null null null null null ",
				".a 1st feb 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/02/2015 null null null null null null ",
				".a 1st february 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/03/2015 null null null null null null ",
				".a 1st march 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/04/2015 null null null null null null ",
				".a 1st apr 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/04/2015 null null null null null null ",
				".a 1st april 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/05/2015 null null null null null null ",
				".a 1st may 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/06/2015 null null null null null null ",
				".a 1st jun 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/06/2015 null null null null null null ",
				".a 1st june 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/07/2015 null null null null null null ",
				".a 1st july 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/07/2015 null null null null null null ",
				".a 1st jul 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/08/2015 null null null null null null ",
				".a 1st aug 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/08/2015 null null null null null null ",
				".a 1st august 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/09/2015 null null null null null null ",
				".a 1st sept 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/09/2015 null null null null null null ",
				".a 1st september 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/10/2015 null null null null null null ",
				".a 1st oct 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/10/2015 null null null null null null ",
				".a 1st october 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/11/2015 null null null null null null ",
				".a 1st nov 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/11/2015 null null null null null null ",
				".a 1st november 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/12/2015 null null null null null null ",
				".a 1st dec 15 hello world");
		testParseInput("test for spelled date format one",
				"add hello world 01/12/2015 null null null null null null ",
				".a 1st december 15 hello world");
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
		testParseInput(
				"test for spelled date format one",
				"add hello 31/03/2014 null null null null null null ",
				".a 31st mar 2014 hello");
		testParseInput("test for spelled date format one",
				"add hello 31/03/2015 null null null null null null ",
				".a 31st mar 15 hello");
		testParseInput(
				"test for spelled date format one",
				"add hello 31/03/2014 null null null null null null ",
				".a 31st mar 14 hello");
		testParseInput("test for spelled date format one",
				"add hello 29/02/2016 null null null null null null ",
				".a 29th february 16 hello");

		testParseInput("test for spelled date format one",
				"add hello 01/03/2015 null null null null null null ",
				".a 1 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 02/03/2015 null null null null null null ",
				".a 2 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 03/03/2015 null null null null null null ",
				".a 3 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 04/03/2015 null null null null null null ",
				".a 4 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 11/03/2015 null null null null null null ",
				".a 11 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 12/03/2015 null null null null null null ",
				".a 12 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 13/03/2015 null null null null null null ",
				".a 13 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 30/03/2015 null null null null null null ",
				".a 30 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 24/03/2015 null null null null null null ",
				".a 24 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 22/03/2015 null null null null null null ",
				".a 22 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 23/03/2015 null null null null null null ",
				".a 23 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 30/03/2015 null null null null null null ",
				".a 30 mar 2015 hello");
		testParseInput("test for spelled date format one",
				"add hello 31/03/2015 null null null null null null ",
				".a 31 mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 0 mar 2015 hello null null null null null invalid Date null ",
				".a 0 mar 2015 hello");
		testParseInput(
				"test for spelled date format one",
				"error 32 mar 2015 hello null null null null null invalid Date null ",
				".a 32 mar 2015 hello"); // tests for format one
		testParseInput("test for date format one",
				"add hello 29/02/2012 null null null null null null ",
				".a 29/2/12 hello");
		testParseInput("test for date format one",
				"error 29/2/15 hello null null null null null invalid Date null ",
				".a 29/2/15 hello");
		testParseInput("test for date format one",
				"add hello 12/05/2015 null null null null null null ",
				".a 12/05/15 hello");
		testParseInput("test for date format one",
				"add hello 29/02/2016 null null null null null null ",
				".a 29/2/16 hello");
		testParseInput("test for date format one",
				"add hello 01/01/2015 null null null null null null ",
				".a 1/1/15 hello");
		testParseInput("test for date format one",
				"add hello 12/04/2014 null null null null null null ",
				".a 12/04/14 hello");
		testParseInput("test for date format one",
				"add 12/05/ hello 04/11/2014 null null null null null null ",
				".a 12/05/ hello");
		testParseInput("test for date format one",
				"add 12/05/2 hello 04/11/2014 null null null null null null ",
				".a 12/05/2 hello");
		testParseInput("test for date format one",
				"add 12/05/123 hello 04/11/2014 null null null null null null ",
				".a 12/05/123 hello");
		// testing search
		/*
		testParseInput(
				"test for search",
				"search the world on 23rd mar hello 24/04/2015 17:00 null null null null ",
				"the world on 23rd mar is at 5pm at 24th apr to be searched .dtl hello  ");
		testParseInput(
				"test for search",
				"search the world on 23rd mar hello .dtl null 17:00 null null null null ",
				"the world on /23rd mar at 5pm to be searched .dtl hello .dtl ");
		testParseInput("test for search",
				"error null null null null null No type found. null ", "search");
		testParseInput("test for search",
				"search null null null hello .dtl null null null ",
				" search .dtl hello .dtl ");
		testParseInput("test for search",
				"search null 24/04/2014 null null null null null ",
				" search for 24th apr 14");
		testParseInput("test for search",
				"search null null 1400 null null null null ",
				" search for 14:00 ");
		testParseInput("test for search",
				"search null null 2300 null null null null ",
				" search for 11pm ");
		testParseInput("test for search",
				"search null null 1215 null null null null ",
				" search for 12.15pm ");
		testParseInput("test for search",
				"search hello cat. null null null null null null ",
				" search hello cat.");
		testParseInput("test for search",
				"search null null null null 3 null null ",
				" search important 3");
		testParseInput("test for search",
				"search null null null null 3 null null ", " search .i 3");
		testParseInput("test for search",
				"search null null null null 3 null null ",
				" search which is of .i 3");

		/*
		 * testParseInput("simple get before any add",
		 * "add hello 29092014 null ", ".a mon hello");
		 * testParseInput("simple get before any add",
		 * "add hello 29092014 null ", ".a Mon hello");
		 * testParseInput("simple get before any add",
		 * "add hello 29092014 null ", ".a MON hello");
		 * testParseInput("simple get before any add",
		 * "add hello 29092014 null ", ".a 12:00 hello");
		 * testParseInput("simple get before any add", "add hello ft null ",
		 * ".a ft hello"); testParseInput("simple get before any add",
		 * "add hello 24092014 null ", ".a hello");
		 * 
		 * 
		 * testParseInput("simple get before any add",
		 * "error invalid Date invalid Date null ", ".a 12/05/2014 hello");
		 */
	}

	private void testParseInput(String description, String expected,
			String command) {
		assertEquals(description, expected,
				TestDriverParser.getParsedInput(command));
	}

}
