//
//
//
//
//
//
//
//

var stXML = false;
var stXML_organism = false;
var stXML_targorg = false;
var stXML_addTarget = false;

var uploader = "";
var idTarget = "";
var idOrganism = "";
var idTargOrg = -1;

var current_origin = 0;

$(document).ready(tableandupload);


function init_stXML()
{
  var ref = false;
  var stelement = false;
  try{ 
   ref = new ActiveXObject('Msxml2.XMLHTTP');
  }catch (e1) { 
   try {
     ref = new ActiveXObject('Microsoft.XMLHTTP');
   }catch (e2) {
     stelement = false;
   }
  }

  if (!ref && (typeof XMLHttpRequest != 'undefined' || window.XMLHttpRequest)) 
   ref = new XMLHttpRequest();  
   
  return ref;
}

function load_combos(j)
{
current_origin = j;
targetOnChange(j);
organismOnChange();
}

function targetOnChange(j)
{
  stXML = init_stXML();
   
  var URL= 'LEIgetTarget?database='+j;
    
  if(stXML) 
  {
    stXML.open('POST', URL, true);
    stXML.onreadystatechange=PopulateTargets;
    stXML.send(null);
  }
}

function PopulateTargets()
{
  if(stXML.readyState == 4)
  {

   if(stXML.status == 200) {
  
    var texto = stXML.responseText;

      var targets = document.getElementById("target"); 
      var responselist = texto.split(";");   
      targets.options.length = 0;
      for(i = 0; i < responselist.length; i++) {  
      var comboitem = document.createElement("option");   
      comboitem.text = responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.text = comboitem.text.replace(new RegExp( "\\n", "g" ),"");
      comboitem.text = comboitem.text.replace(new RegExp( "\\r", "g" ),"");
      comboitem.value = responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.value = comboitem.value.replace(new RegExp( "\\n", "g" ),"");
      comboitem.value = comboitem.value.replace(new RegExp( "\\r", "g" ),"");


      targets.options.add(comboitem);   
      }  
      targets.options.length = targets.options.length - 1;
      targetSelected();

  }
  }
}


function PopulateOrganism()
{
  if(stXML_organism.readyState == 4)
  {

   if(stXML_organism.status == 200) {
   
    var texto = stXML_organism.responseText;

      var organism = document.getElementById("organism");
      var responselist = texto.split(";");
      organism.options.length = 0;
      for(i = 0; i < responselist.length; i++) {
      var comboitem = document.createElement("option");
      comboitem.text = responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.value = responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.text = comboitem.text.replace(new RegExp( "\\n", "g" ),"");
      comboitem.text = comboitem.text.replace(new RegExp( "\\r", "g" ),"");

      comboitem.value = comboitem.value.replace(new RegExp( "\\n", "g" ),"");
      comboitem.value = comboitem.value.replace(new RegExp( "\\r", "g" ),"");


      organism.options.add(comboitem);
      }
      organism.options.length = organism.options.length - 1;

      organismSelected();
  }
  }
}


function organismOnChange()
{
  stXML_organism = init_stXML();

  var URL= 'LEIgetOrganism';

  if(stXML_organism)
  {
    stXML_organism.open('POST', URL, true);
    stXML_organism.onreadystatechange=PopulateOrganism;
    stXML_organism.send(null);
  }
}



function targetSelected()
{
     var responselist = document.getElementById("target").value.split("-");
     idTarget = responselist[0];
//     document.getElementById("descripcion").innerHTML = idTarget
     document.getElementById("idtarget").value = idTarget;
     TheTableFunc();
     init_uploader();
     init_uploader_SMI();


}

function organismSelected()
{
     var responselist = document.getElementById("organism").value.split("-");
     idOrganism = responselist[0];
//     document.getElementById("descripcion").innerHTML = idOrganism;
     document.getElementById("idorganism").value = idOrganism;
     TheTableFunc();
     init_uploader();
     init_uploader_SMI();
}


function PickPair()
{
 targetSelected();
 organismSelected();
 document.getElementById("descripcion").innerHTML = idTarget + " - " + idOrganism;
}


function addTarget()
{
  var newTarget = document.getElementById("NewTarget").value

  stXML_addTarget = init_stXML();

  var URL= 'LEIaddTarget?target='+newTarget;

  if(stXML_addTarget)
  {
    stXML_addTarget.open('POST', URL, true);
    stXML_addTarget.onreadystatechange=ReturnAddTarget;
    stXML_addTarget.send(null);
  }
}

function ReturnAddTarget()
{
  if(stXML_organism.readyState == 4)
  {
    document.getElementById("addTarget_result").innerHTML = stXML_addTarget.responseText;
    targetOnChange(current_origin);
  }

}

function trim (myString)
{
	return myString.replace(/^\s+/g,'').replace(/\s+$/g,'')
}

function TheTableFunc() {
           $('#lei_data').dataTable({
                                      "bProcessing": true,
     				      "bServerSide": true,
				      "bDestroy": true,
                                      "sAjaxSource": "LEIgetMolecules?json=1&target="+trim(idTarget)+"&organism="+trim(idOrganism),
                                        aoColumns: [ { "bVisible": false} , { "bVisible": false}, { "bVisible": false}, null, null, null, null ]
                                    }
                                    ).makeEditable({
                                             sUpdateURL: "LEIupdateCompound",
                                             sAddURL: "LEIaddCompound",
                                             sAddHttpMethod: "POST",
                                             sDeleteURL: "LEIdeleteCompound",
                                             sDeleteHttpMethod: "POST",
					     "aoColumns": [ {}, {}, 
					     { type: 'select',onblur: 'submit',data: "{'Kd':'Kd','Ki':'Ki','IC50':'IC50','EC50/IC50 ratio':'EC50/IC50 ratio','dG0':'dG<sub>0</sub>'}", indicator: 'Saving ...', tooltip: 'select data type', loadtext: 'loading...'}, 
						        {}] 
				   	     });
} 

function init_uploader(){
        var uploader = new qq.FileUploader({
            element: document.getElementById('file-uploader'),
            action: 'LEIuploadCSV?target='+trim(idTarget)+'&organism='+trim(idOrganism)
        });
}

function init_uploader_SMI(){
        var uploader_SMI = new qq.FileUploader({
            element: document.getElementById('file-uploader_SMI'),
            action: 'LEIuploadSMI?target='+trim(idTarget)+'&organism='+trim(idOrganism)
        });

        var uploader_SDF = new qq.FileUploader({
            element: document.getElementById('file-uploader_SDF'),
            action: 'LEIuploadSMI?target='+trim(idTarget)+'&organism='+trim(idOrganism)
        });


}


function tableandupload(){
TheTableFunc();
init_uploader();
init_uploader_SMI();
}

