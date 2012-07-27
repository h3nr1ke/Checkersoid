var path = "file:///android_asset/www/";
//var path = "";
var v = 0;
var sh = 0;
var sw = 0;
var red_piece = "";
var black_piece = "";

$(document).ready(function (){

  $.event.special.swipe.horizontalDistanceThreshold = 80;

  //screen reorder
	$("#game").live('swipeleft swiperight', function(event){
		event.preventDefault();
		restartGame();
	});

	//wait some time to start the pieces to be sure that there is a place to put then
	window.setTimeout(function(){
	    
	  //get the screen dimensions
	  sh = parseInt( $(document).height() / 8);
	  sw = parseInt( $(document).width() / 8);


	  if(sw > sh){
	    v = sh;
	  }else{
	    v = sw;	
	  }
	
	  $("tr td").width(v+"px");
	  	//define the cells size
	  $('td').css('line-height',sh).css('height',sh).css('width',sw);
	  //include all game pieces
	  includePieces();
	  Android.showToast(Android.getTransString("time_to_play"));
	}, 200);
	

  
  //bind the actions
  $('td.black').click(function (){
    if($('td.sel').length > 0 &&  !$(this).is('.piece')){
      //check the sel piece
      dc = 0; //destination count
      if($('td.sel').find('img').is('.red')){
        dc = -1;
      }else if($('td.sel').find('img').is('.black')){
        dc = +1;
      }

      //destiny and selected cell
      ls = parseInt($('td.sel').parent().attr('id'));
      ld = parseInt($(this).parent().attr('id'));
      cs = parseInt($('td.sel').attr('title'));
      ds = parseInt($(this).attr('title'));
      
      el = 0; //linha destino pra comer pra tras
      if((ld - ls) > 0){
      	el = 1;
     	}else{
      	el = -1
      }
	   	sec = 0;
	    if( (ds - cs) > 0){
	      ec = 1; //dir
	    }else{
	      ec = -1; //esq
	    }

      //move only one line and one col left or right
      if(ld == (ls + dc) && ( (cs == (ds+1)) || (cs == (ds-1)))){
          movePiece($(this));
      }
      else if((ls == (ld + (dc*2)) || ld == (ls + (dc*2))) && ( (cs == (ds+2)) || (cs == (ds-2))) && !$('td.sel').find('img').is('.dama')){ //captura de peca
        ec = 0;
        if( (ds - cs) > 0){
          ec = 1; //dir
        }else{
          ec = -1; //esq
        }

        //check and remove if is a piece
        ptr = 'tr#'+(ls+el)+' td[title="'+(cs+ec)+'"]';
        if($(ptr).is('.piece') && 
          ( ($(ptr).find('img').is('.red') && !$('td.sel').find('img').is('.red')) || 
            ($(ptr).find('img').is('.black') && !$('td.sel').find('img').is('.black'))) 
        ){
          $(ptr).find('img').remove();
          $(ptr).removeClass('piece').removeClass('dama');
          movePiece($(this));
        }
      }
      else if( ((Math.abs(ld-ls)/Math.abs(cs-ds)) ==  1) && $('td.sel').find('img').is('.dama') ){
      
      pieces = 0;
      pieceSel = "";
      pieceClass = "";
      //in the way must be just one piece
       if( (ds - cs) > 0){
		      ec = 1; //dir
		    }else{
		      ec = -1; //esq
		    }
		    
      //this pice nee to be the other color
      	if(el > 0){	//somar o numero de linhas
      		//check if there is a piece in the way
      		var i = 0;
	      	for(i=ls+1;i<ld;i++){
	      		if(ec >0){
	      			cs = cs + 1;
	      		}
	      		else{
	      			cs = cs -1;
	      		}
	      		ptr = 'tr#'+(i)+' td[title="'+cs+'"] img';
	      		if( $(ptr).length > 0 ){
	      			pieces++;
	      			pieceSel = ptr;
	      		}

	      	}
      	}
      	else{	//diminuir o numero de linhas

      		var i = 0;
	      	for(i=ls-1;i>ld;i--){
	      		if(ec > 0){
	      			cs = cs + 1;
	      		}
	      		else{
	      			cs = cs -1;
	      		}
	      		ptr = 'tr#'+(i)+' td[title="'+cs+'"] img';
	      		if( $(ptr).length > 0 ){
	      			pieces++;
	      			pieceSel = ptr;
	      		}

	      	}
      	}
      	
      	if(pieces == 1 && !( $('td.sel').find("img").hasClass($(pieceSel).attr("class")) ) ){
      	
         	$(pieceSel).parent().removeClass('piece').removeClass('dama');
	      	$(pieceSel).remove();
	      	movePiece($(this));
	      }
	      		
	  		if(pieces == 0 && pieceSel== ""){
	  			movePiece($(this));
	  		}
      }

    }else{
      $('td').removeClass('sel');
      if($(this).is('.piece')){
        $(this).addClass('sel');
      }
    }
  });
  
  function restartGame(){
  	//remove all pieces
  	$("#area img").remove();
  	includePieces();
  	Android.showToast(Android.getTransString("clean_screen"));
  }
  
  function includePieces(){
	  //include the pieces
		red_piece = '<img class="red" src="'+path+'red.png" height="'+v+'px" width="'+v+'px" />';
		var black_piece = '<img class="black" src="'+path+'black.png" height="'+v+'px" width="'+v+'px" />';
		$("td.piece").removeClass("piece");
		$("td.sel").removeClass("sel");
		$('tr#1 td.black').html(black_piece).addClass("piece");
		$('tr#2 td.black').html(black_piece).addClass("piece");
		$('tr#3 td.black').html(black_piece).addClass("piece");
		
		$('tr#6 td.black').html(red_piece).addClass("piece");
		$('tr#7 td.black').html(red_piece).addClass("piece");
		$('tr#8 td.black').html(red_piece).addClass("piece");
		
		//clona os originais
	/*	var red_piece = $("#red-piece img").clone();
		var black_piece = $("#black-piece img").clone();
		
		red_piece.clone().appendTo('tr#6 td.black');
		red_piece.clone().appendTo('tr#7 td.black');
		red_piece.clone().appendTo('tr#8 td.black');
		
		black_piece.clone().appendTo('tr#1 td.black');
		black_piece.clone().appendTo('tr#2 td.black');
		black_piece.clone().appendTo('tr#3 td.black');*/
		
  }
  
  function movePiece(destiny){
    //move a peca
    h = $('td.sel').html();
    $('td.sel').html('');
    destiny.html(h);

    //update classes in the destiny cell
    c = $('td.sel').attr('class');
    $('td.sel').removeClass('sel').removeClass('piece');
    destiny.attr('class',c);

    //remove a sel class in the end of a move
    $('td').removeClass('sel');
    if(destiny.parent().attr('id') == '1' && destiny.find('img').is('.red') && !destiny.find('img').is('.dama')){
      destiny.find('img').attr('src',path+'red_dama.png').addClass('dama');
    }
    if(destiny.parent().attr('id') == '8' && destiny.find('img').is('.black') && !destiny.find('img').is('.dama')){
      destiny.find('img').attr('src',path+'black_dama.png').addClass('dama');
    }
  }
});