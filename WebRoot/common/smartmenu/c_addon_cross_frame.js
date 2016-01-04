/*
========================================
 Cross Frame Popup Menus v1.0
 Add-on for SmartMenus v6.0+
========================================
 (c)2007 ET VADIKOM-VASIL DINKOV
========================================
*/


c_mainFrameName='mainBody'; // the name of the main frame containing your menus
c_mainFramePosition=1; // 1- bottom, 2- right, 3- top, 4- left


// ===
function c_show(m,e){var f=parent.frames[c_mainFrameName];if(!f||typeof f.c_show=="undefined")return;var u=f.c_gO(m);if(!u||u.IN!=2)return;if(typeof c_dB=="undefined"){c_dB=document.getElementsByTagName("body")[0];c_dE=f.c_qM||f.c_iEM?c_dB:document.documentElement}var c,x,y,sX,sY,eX,eY;c=f.c_gW();sX=window.pageXOffset||c_dE.scrollLeft;sY=window.pageYOffset||c_dE.scrollTop;eX=e.pageX||e.clientX+sX;eY=e.pageY||e.clientY+sY;x=arguments[2]?arguments[2].replace(/mouseX/g,eX).replace(/mouseY/g,eY):eX;y=arguments[3]?arguments[3].replace(/mouseX/g,eX).replace(/mouseY/g,eY):eY;if(c_mainFramePosition==1||c_mainFramePosition==2){x+="+"+c.x;y+="+"+c.y}else if(c_mainFramePosition==3){x+="+"+c.x;y=c.h+c.y+"-menuH"+"-("+y+")"}else if(c_mainFramePosition==4){x=c.w+c.x+"-menuW"+"-("+x+")";y+="+"+c.y}f.c_show(m,e,x,y)};function c_hide(){var f=parent.frames[c_mainFrameName];if(!f||typeof f.c_hide=="undefined")return;f.c_hide()}