$(document).ready( function(){
  var cTime = new Date(), month = cTime.getMonth()+1, year = cTime.getFullYear(), day = cTime.getDay()+1;

	theMonths = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

	theDays = ["S", "M", "T", "W", "T", "F", "S"];
    events = [
      [
        day+"/"+month+"/"+year, 
        'Todays Date', 
        '#', 
        '#177bbb', 
        'RSA Technologies wishes you a nice day.'
      ]
      
    ];
    $('#calendar').calendar({
        months: theMonths,
        days: theDays,
        events: events,
        popover_options:{
            placement: 'top',
            html: true
        }
    });
});