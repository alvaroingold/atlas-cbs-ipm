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
var stXML_database = false;
var stXML_phs = false;
var stXML_addTarget = false;
var stXML_graph_data= false;
var stXML_pdb = false;
var stXML_molinfo = false;

var graph_content = [];
var selection_list = [];
// Subset definitions. Cele feature request on 29/02/2012
var subsets = [];
var subsets_data = [];

var nRows_table_select = "";
var nCells_table_select = "";
var selection_table = "";
var plot1 = "";

var uploader = "";
var idTarget = "";
var idOrganism = "";
var iddatabase ="";
var idphs="";
var idTargOrg = -1;

var current_x = 4;
var current_y = 6;
var lei_labels = ["LEH","BEI","SEI","NSEI","NBEI","nBEI","mBEI","LEP"];

var oTable = "";
var sources_string = "";
var global_data =[ [null,null,null,null] ];

var pdb_loading = 0;
var globalneig = "";

//var jsonurl = "/atlas/servlets/servlet/LEIgetData?json=1&target=372&organism=38&type=3&x=4&y=6";
var jsonurl = "LEIgetData?json=1";



var current_origin = 0;
var current_props_name = "";


function get_molinformation(name){

	  current_props_name = name;
          stXML_molinfo = init_stXML();
          var URL= 'LEIgetMolInformation?molecule='+name;

          if(stXML_molinfo)
          {
            stXML_molinfo.open('POST', URL, true);
            stXML_molinfo.onreadystatechange=Populate_molinformation;
            stXML_molinfo.send(null);
          }
}


function get_molinformation_extended(name,tid,torg,type){

          current_props_name = name;
          stXML_molinfo = init_stXML();
          var URL= 'LEIgetMolInformation?molecule='+name+'&target='+tid+'&organism='+torg+'&type='+type;

          if(stXML_molinfo)
          {
            stXML_molinfo.open('POST', URL, true);
            stXML_molinfo.onreadystatechange=Populate_molinformation;
            stXML_molinfo.send(null);
          }
}



function Populate_molinformation(){
  if(stXML_molinfo.readyState == 4)
  {

   if(stXML_molinfo.status == 200) {

    var texto = stXML_molinfo.responseText;
    var texto2 = texto;
    var value = "Not available"; 
    var phtype = "pK";
    var haspdb = "";

      if( texto.indexOf("|") > 0)
      {
         var text_split = texto.split("|");
         if( text_split.length > 1){
            var secondpart = text_split[1];	
            txtsplit3 = secondpart.split("#");
            phtype = "pK";
            type = parseInt(txtsplit3[0]);
            if( type == 1)
	    {
		phtype = "pK<sub>d</sub>";
	    }else if( type == 2){
		phtype = "pK<sub>i</sub>";
	    }else if( type == 3){
                phtype = "pIC<sub>50</sub>";
	    }
 
            value = txtsplit3[1];
	  }

	  if (phtype == 'pK')
                 value = "Not available"; 

	  if( text_split.length > 2){
	    var secondpart = text_split[2];
            txtsplit3 = secondpart.split("#");
	    pdb=txtsplit3[1];
	    haspdb='<TR><TD>PDB</TD><TD><A HREF=\"http://www.pdb.org/pdb/explore/explore.do?structureId='+pdb+'\">'+pdb+'</A></TD></TR>';
	  }

          texto2 = text_split[0];
      }

       var responselist = texto2.split(";");
       var polar = responselist[0];
       var mass = responselist[1];
       var natoms = responselist[2];
       var heavy = responselist[3];
       var psa = responselist[4];
       var name = responselist[5];
       var rpolar = polar.split("#");
       var npolar = rpolar[1];
       var dbtype = rpolar[2];
	var rmass = mass.split("#");
        var nmass = parseFloat(rmass[1]).toFixed(2);
	var rpsa = psa.split("#");
	var npsa = parseFloat(rpsa[1]).toFixed(2);
	var rheavy = heavy.split("#");
	var nheavy = rheavy[1];
	if( (dbtype == 'BindingDB' || dbtype=='BINDINGDB') && current_props_name.indexOf("ChEMBL") != 0 && current_props_name.indexOf("CHEMBL") != 0){
		document.getElementById("mol_props").innerHTML =  '<CENTER><TABLE BORDER=0><TR><TD>Name</TD><TD>'+current_props_name+'</TD></TR><TR><TD>Mass</TD><TD>'+nmass+'</TD></TR><TR><TD>Polar atoms</TD><TD>'+npolar+'</TD></TR><TR><TD>TPSA</TD><TD>'+npsa+'</TD></TR><TR><TD>Heavy Atoms</TD><TD>'+nheavy+'</TD></TR> <TR><TD>'+phtype+'</TD><TD>'+value+'</TD></TR>  <TR><TD>Link</TD><TD><A HREF="http://www.bindingdb.org/bind/searchby_r1l.jsp?constrain=0&reactant1='+current_props_name+'&tag=r1l&loMW=&hiMW=&loKI=&hiKI=&loIC=&hiIC=&lodG=&hidG=&anDor=and&submit=Search" target="_blank">'+dbtype+'</A></TD></TR>'+haspdb+'</TABLE></CENTER>';
        }else if( dbtype == 'ChEMBL' || current_props_name.indexOf("ChEMBL") == 0 || current_props_name.indexOf("CHEMBL") == 0){
                document.getElementById("mol_props").innerHTML =  '<center><TABLE BORDER=0><TR><TD>Name</TD><TD>'+current_props_name+'</TD></TR><TR><TD>Mass</TD><TD>'+nmass+'</TD></TR><TR><TD>Polar atoms</TD><TD>'+npolar+'</TD></TR><TR><TD>TPSA</TD><TD>'+npsa+'</TD></TR><TR><TD>Heavy Atoms</TD><TD>'+nheavy+'</TD></TR> <TR><TD>'+phtype+'</TD><TD>'+value+'</TD></TR>  <TR><TD>Link</TD><TD><A HREF="https://www.ebi.ac.uk/chembldb/compound/inspect/'+current_props_name.replace("id","")+'" target="_blank">ChEMBL</A></TD></TR>'+haspdb+'</TABLE></center>';
	}else if( dbtype == 'PDBBind' || dbtype == "PDBBIND"){
document.getElementById("mol_props").innerHTML =  '<center><TABLE BORDER=0><TR><TD>Name</TD><TD>'+current_props_name+'</TD></TR><TR><TD>Mass</TD><TD>'+nmass+'</TD></TR><TR><TD>Polar atoms</TD><TD>'+npolar+'</TD></TR><TR><TD>TPSA</TD><TD>'+npsa+'</TD></TR><TR><TD>Heavy Atoms</TD><TD>'+nheavy+'</TD></TR> <TR><TD>'+phtype+'</TD><TD>'+value+'</TD></TR>  <TR><TD>Link</TD><TD><A HREF="http://www.pdb.org/pdb/ligand/ligandsummary.do?hetId='+current_props_name+'" target="_blank">'+dbtype+'</A></TD></TR>'+haspdb+'</TABLE></center>';
        }else{
                document.getElementById("mol_props").innerHTML =  '<center><TABLE BORDER=0><TR><TD>Name</TD><TD>'+current_props_name+'</TD></TR><TR><TD>Mass</TD><TD>'+nmass+'</TD></TR><TR><TD>Polar atoms</TD><TD>'+npolar+'</TD></TR><TR><TD>TPSA</TD><TD>'+npsa+'</TD></TR><TR><TD>Heavy Atoms</TD><TD>'+nheavy+'</TD></TR><TR><TD>'+phtype+'</TD><TD>'+value+'</TD></TR>'+haspdb+'</TABLE></center>';
	}
    }

  }
}

function update_x_coord(){

var x_coor = document.getElementById("x_type");
current_x = x_coor.options[x_coor.selectedIndex ].value;
update_json_source();
}

function update_y_coord(){

var y_coor = document.getElementById("y_type");
current_y = y_coor.options[y_coor.selectedIndex ].value;
update_json_source();
}


	function selection_table_getCoords(currCell){

		for (i=0; i<nRows_table_select; i++)
			{
			 for (n=0; n<nCells_table_select; n++)
				{
				 if (selection_table.rows[i].cells[n] == currCell)
					{
						if( i > 0)
						{
						document.getElementById("mol_info").innerHTML = '<IMG align="center" src="jsp/marvin/generate_image.jsp?mol='+escape(selection_list[i-1][3]).replace("+","")+'&format=jpeg:w250,h250">';
				                get_molinformation(selection_list[i-1][2]);

						}

					}
				}
			}
	}

function clear_selection()
{
selection_list = [];
update_selection_table();

	for( i = 0; i < plot1.data.length; ++i)
	{
		for( j = 0; j < plot1.data[i].length; ++j)
		{
			plot1.series[i].data[j][4] = ""

		}
	}

	plot1.redraw();
}

function update_selection_table()
{
  selection_table = document.getElementById('selectionTable');
  tbl = selection_table;

  while(tbl.rows.length>1) 
    tbl.deleteRow(tbl.rows.length-1); 

  for( i = 0; i < selection_list.length; ++i)
  {
	  var row = tbl.insertRow(i+1);
	  var cell = row.insertCell(0);
	  var text = document.createTextNode(selection_list[i][2]);
	  cell.appendChild(text);
	  var cell = row.insertCell(1);
	  var text = document.createTextNode(selection_list[i][3]);
	  cell.appendChild(text);
	  var cell = row.insertCell(2);
	  var text = document.createTextNode(selection_list[i][0]);
	  cell.appendChild(text);
	  var cell = row.insertCell(3);
	  var text = document.createTextNode(selection_list[i][1]);
	  cell.appendChild(text);
  }


		nRows_table_select = tbl.rows.length;
		nCells_table_select = tbl.rows[0].cells.length;
		for (i=0; i<nRows_table_select; i++)
			{
			 for (n=0; n<nCells_table_select; n++)
				{
				 tbl.rows[i].cells[n].onclick = function()
					{
					 selection_table_getCoords(this);				 	
					}
				 tbl.rows[i].cells[n].onmouseover = function()
					{
					 this.style.cursor = 'pointer';			 	
                                         selection_table_getCoords(this);
					}
				}
			}

}

function update_selection_graph()
{
		
	for(j = 0; j < plot1.data.length; ++j)
	{
		for( k = 0; k < plot1.data[j].length; ++k)
		{

			  selected = false;
			  for( i = 0; i < selection_list.length; ++i)
			  {

				if( trim(selection_list[i][2]) == trim(plot1.series[j].data[k][2]))
				{
		                        plot1.series[j].data[k][4] = plot1.series[j].data[k][2];
					selected = true;
				}
		          }

			  if( !selected)
				plot1.series[j].data[k][4] = "";
		}
	
	}

	plot1.redraw();

}




function selection_table_onClick()
{


}

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

function load_stuff(iduser,code)
{
	graph_load();
	databaseOnChange(iduser);
	if(code != null && code != "null")
	{
		load_pdb_code(code);
	}
}


function load_pdb_code(code)
{

	if( pdb_loading == 0)
        {
	  pdb_loading = 1;
	  stXML_pdb = init_stXML();
	  var URL= 'LEIgetPDB?code='+code;

	  if(stXML_pdb)
	  {
	    stXML_pdb.open('POST', URL, true);
	    stXML_pdb.onreadystatechange=PopulatePDB;
	    stXML_pdb.send(null);
	  }
        }
}


function addPDBsource()
{
        pdbcode = document.getElementById("pdbcode").value;
	pdb_loading = 0;
	load_pdb_code(pdbcode);
}

function PopulatePDB()
{

  if(stXML_pdb.readyState == 4)
  {

   if(stXML_pdb.status == 200) {

    var texto = stXML_pdb.responseText;

      var responselist = texto.split(";");

       var idt = responselist[0];
       var ido = responselist[1];
       var idd = responselist[2];
       var idp = responselist[3];
       var name_target = responselist[4];
       var name_organism = responselist[5];
       var name_mol = responselist[6];
       var smiles = responselist[7];

      if( idt != 0)
      {
	      var ds = document.getElementById("datasources");
	      var item = document.createElement("option");
	      item.text = idt + " - " + name_target + " - " + ido + " - " + name_organism;
	      item.value = idt + "#" + ido + "#" + idp;
	      ds.options.add(item);
	      sources_string=sources_string +idt+"#"+ido+"#"+idp+";";
	      update_json_source();
	      refresh_graph();
	      TheTableFunc();

// Selection of the compound from the PDB. Cele & Peter Rose request on end April 2012
	var element = ["PDB", "PDB", name_mol, smiles];
        selection_list.push(element);

	update_selection_table();
	update_selection_graph();

        if( name_mol != null){
                document.getElementById("mol_info").innerHTML = '<IMG align="center" src="jsp/marvin/generate_image.jsp?mol='+escape(smiles.replace("+",""))+'&format=jpeg:w250,h250" align="left">';
                get_molinformation_extended(name_mol,idt,ido,idp);

        }


      }else{


       var $dialog = $('<div></div>')
                .html('<p>Sorry! This PDB code could not extract any affinity data from BindingDB. Consult with <A HREF="http://www.bindingdb.org/">BindingDB</A> or <A HREF="http://www.pdb.org">PDB</A> before you try another one. Make sure that it corresponds to a target-ligand pair PDB entry.</p>')
                .dialog({
                        autoOpen: true,
                        title: 'PDB code',
                        resizable: true,
                        width: 500,
                        height:200,
                        modal: false
                });
      }

   }
  }

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
/*      comboitem.text = responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.text = comboitem.text.replace(new RegExp( "\\n", "g" ),"");
      comboitem.text = comboitem.text.replace(new RegExp( "\\r", "g" ),"");
*/

      var tg=responselist[i];
      tgv = tg.split("#");
      tgname = tgv[1];
	comboitem.text = tgname;
/*      comboitem.text = tgname.replace(new RegExp( "\\n", "g" ),"");
      comboitem.text = comboitem.text.replace(new RegExp( "\\r", "g" ),"");*/


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


function PopulateDatabase()
{

  if(stXML_database.readyState == 4)
  {

   if(stXML_database.status == 200) {

    var texto = stXML_database.responseText;

      var database = document.getElementById("database");
      var responselist = texto.split(";");
      database.options.length = 0;
      for(i = 0; i < responselist.length; i++) {
      var comboitem = document.createElement("option");
/*      comboitem.text = responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.text = comboitem.text.replace(new RegExp( "\\n", "g" ),"");
      comboitem.text = comboitem.text.replace(new RegExp( "\\r", "g" ),"");
*/

      var dbnm=responselist[i];
      var dbonlyname=dbnm.split("#");
      comboitem.text = dbonlyname[1];

      comboitem.value = responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.value = comboitem.value.replace(new RegExp( "\\n", "g" ),"");
      comboitem.value = comboitem.value.replace(new RegExp( "\\r", "g" ),"");


      database.options.add(comboitem);
      }
      database.options.length = database.options.length - 1;
      databaseSelected();

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
/*      comboitem.text = responselist[i].replace(new RegExp( "\#", "g" )," \- ");
*/
      var orgnm=responselist[i];
      var orgonlyname=orgnm.split("#");
      comboitem.text = orgonlyname[1];
/*      comboitem.text = comboitem.text.replace(new RegExp( "\\n", "g" ),"");
      comboitem.text = comboitem.text.replace(new RegExp( "\\r", "g" ),"");
*/
      comboitem.value = responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.value = comboitem.value.replace(new RegExp( "\\n", "g" ),"");
      comboitem.value = comboitem.value.replace(new RegExp( "\\r", "g" ),"");


      organism.options.add(comboitem);
      }
      organism.options.length = organism.options.length - 1;

      organismSelected();
  }
  }
}

function PopulatePhs()
{
  if(stXML_phs.readyState == 4)
  {

   if(stXML_phs.status == 200) {

    var texto = stXML_phs.responseText;

      var phs = document.getElementById("phs");
      var responselist = texto.split(";");
      phs.options.length = 0;
      for(i = 0; i < responselist.length; i++) {

	if( i != 0){

	if( i == 1){
      var comboitem = document.createElement("option");
      comboitem.text = "Kd: " + responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.value = "1 - Kd: " +responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.text = comboitem.text.replace(new RegExp( "\\n", "g" ),"");
      comboitem.text = comboitem.text.replace(new RegExp( "\\r", "g" ),"");

      comboitem.value = comboitem.value.replace(new RegExp( "\\n", "g" ),"");
      comboitem.value = comboitem.value.replace(new RegExp( "\\r", "g" ),"");
	}else if( i == 2){
      var comboitem = document.createElement("option");
      comboitem.text = "Ki: " + responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.value = "2 - Ki: " + responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.text = comboitem.text.replace(new RegExp( "\\n", "g" ),"");
      comboitem.text = comboitem.text.replace(new RegExp( "\\r", "g" ),"");

      comboitem.value = comboitem.value.replace(new RegExp( "\\n", "g" ),"");
      comboitem.value = comboitem.value.replace(new RegExp( "\\r", "g" ),"");
	}else if( i == 3){
      var comboitem = document.createElement("option");
      comboitem.text = "IC50: " + responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.value = "3 - IC50: " + responselist[i].replace(new RegExp( "\#", "g" )," \- ");
      comboitem.text = comboitem.text.replace(new RegExp( "\\n", "g" ),"");
      comboitem.text = comboitem.text.replace(new RegExp( "\\r", "g" ),"");

      comboitem.value = comboitem.value.replace(new RegExp( "\\n", "g" ),"");
      comboitem.value = comboitem.value.replace(new RegExp( "\\r", "g" ),"");
	}

      phs.options.add(comboitem);
	}
      }
//      phs.options.length = phs.options.length - 1;

      phsSelected();

  }
  }
}


function graph_load()
{
  stXML_graph_data = init_stXML();

  var URL= 'LEIgetData?json=1&target=372&organism=38&type=3&x=3&y=3';

  if(stXML_graph_data)
  {
    stXML_graph_data.open('POST', URL, false);
    stXML_graph_data.onreadystatechange=GetGraphData;
    stXML_graph_data.send(null);
  }
}

function GetGraphData()
{
  if(stXML_graph_data.readyState == 4)
  {

   if(stXML_graph_data.status == 200) {
    graph_content = stXML_graph_data.responseText;
   }
  }
}


function organismOnChange()
{
  stXML_organism = init_stXML();

     var responselist = document.getElementById("target").value.split("-");
     idt = responselist[0];

  var URL= 'LEIgetOrganism?target='+idt;

  if(stXML_organism)
  {
    stXML_organism.open('POST', URL, true);
    stXML_organism.onreadystatechange=PopulateOrganism;
    stXML_organism.send(null);
  }
}


function databaseOnChange(iduser)
{
  stXML_database = init_stXML();

 var URL= 'LEIgetDatabases';

	if( parseInt(iduser) > 0)
          URL = 'LEIgetDatabases?iduser='+iduser;

  if(stXML_database)
  {
    stXML_database.open('POST', URL, true);
    stXML_database.onreadystatechange=PopulateDatabase;
    stXML_database.send(null);
  }

}

function databaseSelected()
{
     var responselist = document.getElementById("database").value.split("-");
     iddatabase = responselist[0];
     load_combos(iddatabase);
}


function phsOnChange()
{
  stXML_phs = init_stXML();
    var responselista = document.getElementById("target").value.split("-");
     idt = responselista[0];
    var responselistb = document.getElementById("organism").value.split("-");
     ido = responselistb[0];

  var URL= 'LEIgetAvailableTypes?target='+trim(idt)+'&organism='+trim(ido);

  if(stXML_phs)
  {
    stXML_phs.open('POST', URL, true);
    stXML_phs.onreadystatechange=PopulatePhs;
    stXML_phs.send(null);
  }

}

function phsSelected()
{
     var responselist = document.getElementById("phs").value.split("-");
     idphs = responselist[0];
}



function targetSelected()
{
     var responselist = document.getElementById("target").value.split("-");
     idTarget = responselist[0];
//     document.getElementById("descripcion").innerHTML = idTarget
	organismOnChange()
     TheTableFunc();
}

function organismSelected()
{
     var responselist = document.getElementById("organism").value.split("-");
     idOrganism = responselist[0];
//     document.getElementById("descripcion").innerHTML = idOrganism;
     phsOnChange();
     TheTableFunc();
}


function PickPair()
{
 targetSelected();
 organismSelected();
 document.getElementById("descripcion").innerHTML = idTarget + " - " + idOrganism;
}


/* Bug fixed. Last element removal fail. Cele request on 27/02/2012 */
function removeSource()
{
      var ds = document.getElementById("datasources");

      sources_string = "";

      if( ds.options.length > 1)
      {
      
	      for( i = 0; i < ds.options.length; ++i)
	      {
	        if( i != ds.selectedIndex) 
		sources_string = sources_string + trim(ds.options[i].value)+ ";";
	      }
	      update_json_source();

      }else{
		jsonurl = "LEIgetData?json=1";
                plot1.data = [];
		plot1.series = [];
		setInit();
	        update_selection_graph();
	        TheTableFunc();

      }

      ds.options.remove(ds.selectedIndex);

}

/* Hide or show the selected set. Cele feature request on 27/02/2012 */
function hideSource()
{
      var ds = document.getElementById("datasources");
      var toggle = plot1.series[ds.selectedIndex].show
      if( toggle) 
	      plot1.series[ds.selectedIndex].show = false;
      else
	      plot1.series[ds.selectedIndex].show = true;
      plot1.replot()

}

function update_json_source()
{
	jsonurl="LEIgetData?json=1&x="+ current_x +"&y="+ current_y + "&sources="+escape(sources_string);

	refresh_graph();
	update_selection_graph();
	TheTableFunc();
}

function addSource()
{
  var idt = trim(idTarget);
  var ido = trim(idOrganism);
  var idd = trim(iddatabase);
  var idp = trim(idphs);

      var ds = document.getElementById("datasources");
      var item = document.createElement("option");
      item.text = document.getElementById("target").value + " - " + document.getElementById("organism").value
      item.value = trim(document.getElementById("target").value.split("-")[0]) + "#" + trim(document.getElementById("organism").value.split("-")[0]) + "#" + trim(document.getElementById("phs").value.split("-")[0]);
      ds.options.add(item);

  sources_string=sources_string +idt+"#"+ido+"#"+idp+";";
  update_json_source();
  TheTableFunc();
}

/*
function ReturnAddTarget()
{
  if(stXML_organism.readyState == 4)
  {
    document.getElementById("addTarget_result").innerHTML = stXML_addTarget.responseText;
    targetOnChange(current_origin);
  }

}*/

function trim (myString)
{
	return myString.replace(/^\s+/g,'').replace(/\s+$/g,'')
}

function fnGetSelected( oTableLocal )
{
    var aReturn = new Array();
    var aTrs = oTableLocal.fnGetNodes();
     
    for ( var i=0 ; i<aTrs.length ; i++ )
    {
        if ( $(aTrs[i]).hasClass('row_selected') )
        {
            aReturn.push( aTrs[i] );
        }
    }
    return aReturn;
}


function TheTableFunc() {

/*
    $('#lei_data').dataTable( {
        "bProcessing": false,
        "bDestroy": true,
        "aaData": [
            [ "Trident", "Internet Explorer 4.0", "Win 95+", "abc" ],
            [ "Trident", "Internet Explorer 5.0", "Win 95+", "abc"]
        ]
    } );    */



    oTable = $('#lei_data').dataTable( {
        "bProcessing": false,
        "bDestroy": true,
        "aaData": global_data
    } );


    $("#lei_data tbody").click(function(event) {
	sel_item = []

       $(oTable.fnSettings().aoData).each(function (){
            $(this.nTr).removeClass('row_selected');
        });
        $(event.target.parentNode).addClass('row_selected');

        sel_item.push(event.target.parentNode.cells.item(2).innerHTML);
        sel_item.push(event.target.parentNode.cells.item(3).innerHTML);
	sel_item.push(event.target.parentNode.cells.item(0).innerHTML);
        sel_item.push(event.target.parentNode.cells.item(1).innerHTML);

	if( !check_duplicates( sel_item ))
	{
	        selection_list.push(sel_item);
		update_selection_table();
	        update_selection_graph();
	}

    });



	




/*           $('#lei_data').dataTable({
                                      "bProcessing": true,
     				      "bServerSide": true,
				      "bDestroy": true,
                                      "sAjaxSource": "LEIgetMoleculesExt?json=1&x="+current_x+"&y="+current_y+"&"+sources="+trim(sources_string),
                                        aoColumns: [ null, null, null, null ]
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
				   	     });*/
} 

function tableandupload(){
TheTableFunc();
}

function myClickHandler(ev, gridpos, datapos, neighbor, plot)
{
        if( neighbor != null){

                document.getElementById("mol_info").innerHTML = '<IMG align="center" src="jsp/marvin/generate_image.jsp?mol='+escape(neighbor.data[3]).replace("+","")+'&format=jpeg:w250,h250">';


                indx = neighbor.seriesIndex;

                var ds = document.getElementById("datasources");

                if( ds.options.length > 0)
                {

                        strcon = ds.options[indx].value;
                        parts = strcon.split("#");
                        get_molinformation_extended(neighbor.data[2],parts[0],parts[1],parts[2]);

                }else{
                        get_molinformation(neighbor.data[2]);
                }

//		get_molinformation(neighbor.data[2]);


                if( neighbor.data[4] != "")
                {
                        neighbor.data[4] = "";
                }else{
                        neighbor.data[4] = neighbor.data[2];
                        selection_list.push(neighbor.data);
                        update_selection_table();
                }
                plot1.redraw();
        }
}


function myOverHandler(ev, gridpos, datapos, neighbor, plot)
{
        if( neighbor != null){
                document.getElementById("mol_info").innerHTML = '<IMG align="center" src="jsp/marvin/generate_image.jsp?mol='+escape(neighbor.data[3]).replace("+","")+'&format=jpeg:w250,h250" align="left">';

		indx = neighbor.seriesIndex;

	      	var ds = document.getElementById("datasources");

	      	if( ds.options.length > 0)
	      	{

			strcon = ds.options[indx].value;
			parts = strcon.split("#");
			get_molinformation_extended(neighbor.data[2],parts[0],parts[1],parts[2]);

	      	}else{
	                get_molinformation(neighbor.data[2]);
		}

        }
}




function createChart(elementName, data){

  plot = $.jqplot('chart', data, {
    seriesDefaults: {
      showLine:false,
      pointLabels:{ show:true, location:'s' }
    },
      title:'Map',
      axes:{
        xaxis:{
        },
        yaxis:{
        }
      },
      highlighter: {
    tooltipAxes: 'y',
    yvalues: 3,
        show: true,
        sizeAdjust: 7.5,
        formatString: '<!-- %s -->%s<br> <!-- <IMG src="jsp/marvin/generate_image.jsp?mol=i\'+escape(%s).replace("+","")+\'&format=jpeg:w150,h150"> -->'
      },
      cursor: {
        zoom:true,
       show: true,
        tooltipLocation:'sw'
      }
  });


  return plot;
}


  function aj(url) {
    var ret = null;
    $.ajax({
      // have to use synchronous here, else the function
      // will return before the data is fetched
      async: false,
      url: url,
      dataType:"json",
      success: function(data) {

	global_data = [];
	for( i = 0; i < data.length; ++i)
	{
		for( j = 0; j < data[i].length; ++j)
		{
			element = [];
			if( data[i][j].length == 0)
			{	
				element = [null,null,null,null];
			}else{
			element.push(data[i][j][2]);
                        element.push(data[i][j][3]);
                        element.push(data[i][j][0]);
                        element.push(data[i][j][1]);
			}
			global_data.push(element);
		}
	}
//	alert("SETS " + subsets.length);

	if( subsets.length != 0)
	{
		a = []
		a.push(1);
		a.push(1);
		a.push("c1ccccc1");
		a.push("benzene");
		
	}
//	for ( i = 0; i < subsets.length; i++)
//	{	
//		var cr_subset = []
//		alert(subsets_data[i].length);
//		for( j = 0; j < subsets_data[i].length; j++)
//		{
//			i_index = subsets_data[i][0];
 //                       j_index = subsets_data[i][1];
//			var element = data[i_index][j_index];
//			cr_subset.push(element);
//			alert(element);
//		}
//		alert("SUBSETS " + cr_subset);
//		data.push(cr_subset);
//	}
//	alert(data);
plot1 = $.jqplot('chart', data, {
    seriesDefaults: {
      showLine:false,
      pointLabels:{ show:true, location:'s' }
    },
      title:'Map',
      axes:{
        xaxis:{
		label: lei_labels[current_x-1],
		labelRenderer: $.jqplot.CanvasAxisLabelRenderer
        },
        yaxis:{
		label: lei_labels[current_y-1],
		labelRenderer: $.jqplot.CanvasAxisLabelRenderer
        }
      },
      highlighter: {
    tooltipAxes: 'y',
    yvalues: 3,
        show: true,
        sizeAdjust: 7.5,
        formatString: '<!-- %s -->%s<br> <!-- <IMG src="jsp/marvin/generate_image.jsp?mol=%s&format=jpeg:w150,h150"> -->'
      },
      cursor: {
        zoom:true,
       show: true,
        tooltipLocation:'sw'
      }
  });
      }
    });

	plot1.replot();

    return plot1;
  }



function setInit() {

	$("#chart").text('');
	plot1  = aj(jsonurl);
}

function refresh_graph(){

        $('#chart').empty(); 
        plot1  = aj(jsonurl);
}
			

function check_duplicates( sel_item )
{
	for( i = 0; i < selection_list.length; ++i)
	{
		if( sel_item[2] == selection_list[i][2])
			return true;
	}
	return false;
}

function append_subsets()
{
	
}

function extract_filters()
{
        var name_filters = document.getElementById("Namefiltering").value;
        var smi_filters = document.getElementById("SMILESfiltering").value;
        var from_x_filters = document.getElementById("FromXfiltering").value;
        var to_x_filters = document.getElementById("ToXfiltering").value;
        var from_y_filters = document.getElementById("FromYfiltering").value;
        var to_y_filters = document.getElementById("ToYfiltering").value;
        var npol_filters = document.getElementById("NPOLfiltering").value;

        var from_x = parseFloat(document.getElementById("FromXfiltering").value);
        var to_x = parseFloat(document.getElementById("ToXfiltering").value);
        var from_y = parseFloat(document.getElementById("FromYfiltering").value);
        var to_y = parseFloat(document.getElementById("ToYfiltering").value);
        var npol_fil = parseInt(document.getElementById("NPOLfiltering").value);

        name_on = false;
        smi_on = false;
        fx_on = false;
        fy_on = false;
        fx_off = false;
        fy_off = false;
        npol_on = false;
        name_on = (name_filters.length > 0) ? true : false;
        smi_on = (smi_filters.length > 0) ? true : false;
        fx_on = (from_x_filters.length > 0) ? true : false;
        fy_on = (from_y_filters.length > 0) ? true : false;
        fx_off = (to_x_filters.length > 0) ? true : false;
        fy_off = (to_y_filters.length > 0) ? true : false;
        npol_on = ( npol_filters.length > 0) ? true: false;

	nsel = 0;
	
	seldata = []
        for( i = 0; i < plot1.data.length; ++i)
        {
                for( j = 0; j < plot1.data[i].length; ++j)
                {
                        add_flag = [true,true,true,true,true,true,true];

                        current_x_el = parseFloat(plot1.series[i].data[j][0]);
                        current_y_el = parseFloat(plot1.series[i].data[j][1]);
                        current_name = plot1.series[i].data[j][2];
                        current_smiles = plot1.series[i].data[j][3];

                        if( name_on )
                        {
                         if(current_name.search(trim(name_filters)) >= 0)
                                add_flag[0] = true;
                         else
                                add_flag[0] = false;
                        }

                        if( smi_on )
                        {
                         if(current_smiles.search(trim(smi_filters)) >= 0)
                                add_flag[1] = true;
                         else
                                add_flag[1] = false;
                        }



                        if( fx_on)
                        {
                                if( current_x_el >= from_x ){
                                        add_flag[2] = true;
                                }else{
                                        add_flag[2] = false;
                                }
                        }

                        if( fy_on )
                        {
                         if(current_y_el >= from_y )
                                add_flag[3] = true;
                         else
                                add_flag[3] = false;
                        }

                        if( fx_off)
                        {
                         if(current_x_el <= to_x )
                                add_flag[4] = true;
                         else
                                add_flag[4] = false;
                        }

                        if( fy_off )
                        {
                         if(current_y_el <= to_y )
                                add_flag[5] = true;
                         else
                                add_flag[5] = false;
                        }


                        if( npol_on)
                        {
                            if( Math.round( current_y_el / current_x_el) == npol_fil)
                                add_flag[6] = true;
                            else
                                add_flag[6] = false;
                        }


                        if( add_flag[0] && add_flag[1] && add_flag[2] && add_flag[3] && add_flag[4] && add_flag[5] && add_flag[6])
                        {
                                var element = [i, j];
				seldata.push(element);
                                nsel = nsel + 1;
                        }

                }
        }

				subsets_data.push(seldata);
                                var el = ["Selected elements ("+nsel+")", "Selected elements"+nsel+")"];
                                subsets.push(el);

//				alert(el);


	update_subsets();
//        update_selection_table();
//        update_selection_graph();

}

function update_subsets()
{
	var ss = document.getElementById("subsetsources")
	ss.options.length =0;
        for( i = 0; i < subsets.length; ++i)
        {
	      var item = document.createElement("option");
	      item.text = subsets[i][0];
	      item.value = subsets[i][1];
	      ss.options.add(item);
	}




}

function set_filters()
{

	var name_filters = document.getElementById("Namefiltering").value;
        var smi_filters = document.getElementById("SMILESfiltering").value;
	var from_x_filters = document.getElementById("FromXfiltering").value;
        var to_x_filters = document.getElementById("ToXfiltering").value;
        var from_y_filters = document.getElementById("FromYfiltering").value;
        var to_y_filters = document.getElementById("ToYfiltering").value;
	var npol_filters = document.getElementById("NPOLfiltering").value;
	
        var from_x = parseFloat(document.getElementById("FromXfiltering").value);
        var to_x = parseFloat(document.getElementById("ToXfiltering").value);
        var from_y = parseFloat(document.getElementById("FromYfiltering").value);
        var to_y = parseFloat(document.getElementById("ToYfiltering").value);
	var npol_fil = parseInt(document.getElementById("NPOLfiltering").value);
	
	name_on = false;
	smi_on = false;
	fx_on = false;
	fy_on = false;
	fx_off = false;
	fy_off = false;
	npol_on = false;


	name_on = (name_filters.length > 0) ? true : false;
        smi_on = (smi_filters.length > 0) ? true : false;
        fx_on = (from_x_filters.length > 0) ? true : false;
        fy_on = (from_y_filters.length > 0) ? true : false;
        fx_off = (to_x_filters.length > 0) ? true : false;
        fy_off = (to_y_filters.length > 0) ? true : false;
	npol_on = ( npol_filters.length > 0) ? true: false;

	
        for( i = 0; i < plot1.data.length; ++i)
        {
                for( j = 0; j < plot1.data[i].length; ++j)
                {
			add_flag = [true,true,true,true,true,true,true];

                        current_x_el = parseFloat(plot1.series[i].data[j][0]);
                        current_y_el = parseFloat(plot1.series[i].data[j][1]);
			current_name = plot1.series[i].data[j][2];
			current_smiles = plot1.series[i].data[j][3];

                        if( name_on )
                        {
                         if(current_name.search(trim(name_filters)) >= 0)
                                add_flag[0] = true;
                         else
                                add_flag[0] = false;
                        }

                        if( smi_on )
                        {
                         if(current_smiles.search(trim(smi_filters)) >= 0)
                                add_flag[1] = true;
                         else
                                add_flag[1] = false;
                        }



			if( fx_on) 
			{
				if( current_x_el >= from_x ){
					add_flag[2] = true;
				}else{
		                        add_flag[2] = false;
				}
			}

                        if( fy_on ) 
			{
			 if(current_y_el >= from_y )
                                add_flag[3] = true;
                         else
                                add_flag[3] = false;
			}

                        if( fx_off)
			{
			 if(current_x_el <= to_x )
                                add_flag[4] = true;
                         else
                                add_flag[4] = false;
			}

                        if( fy_off ) 
			{
			 if(current_y_el <= to_y )
                                add_flag[5] = true;
                         else
                                add_flag[5] = false;
			}


			if( npol_on)
			{
			    if( Math.round( current_y_el / current_x_el) == npol_fil)
				add_flag[6] = true;
			    else
                                add_flag[6] = false;
			}


			if( add_flag[0] && add_flag[1] && add_flag[2] && add_flag[3] && add_flag[4] && add_flag[5] && add_flag[6])
			{
				var element = [current_x_el, current_y_el, current_name, current_smiles];
//				if( !check_duplicates( element ))
	                        selection_list.push(element);
			}

                }
        }

        update_selection_table();
	update_selection_graph();
//        plot1.redraw();


}


function save_view_state()
{
        $.cookie("ATLASsaved", 1);
	$.cookie("ATLASsources", sources_string);


	selection = "";
	for( i = 0; i < selection_list.length; ++i)
	{
		if( i == 0)
		 selection = selection_list[i][0] + "|" + selection_list[i][1] + "|" + selection_list[i][2] + "|" + selection_list[i][3];
		else
		 selection = selection + "|" + selection_list[i][0] + "|" + selection_list[i][1] + "|" + selection_list[i][2] + "|" + selection_list[i][3];
	}
        $.cookie("ATLASselectionlist", selection);


        $.cookie("ATLASX", current_x);
        $.cookie("ATLASY", current_y);

	tmp_ds = []
        var ds = document.getElementById("datasources");
	for( i = 0; i < ds.options.length; ++i)
	{
		tmp_ds.push(ds.options[i].value + "|" + ds.options[i].text);
	}
        $.cookie("ATLASsourceslist", tmp_ds);

}



function clear_view_state()
{
        $.cookie("ATLASsaved", null);
        $.cookie("ATLASsources", null);
        $.cookie("ATLASselectionlist", null);
        $.cookie("ATLASX", null);
        $.cookie("ATLASY", null);
        $.cookie("ATLASsourceslist", null);


}


function load_view_state()
{


	if( $.cookie("ATLASsaved") == 1)
	{
		current_x = $.cookie("ATLASX");
		current_y = $.cookie("ATLASY");
		sources_string = $.cookie("ATLASsources");
		selection_list_temp = $.cookie("ATLASselectionlist")


                var tmp_ds = $.cookie("ATLASsourceslist").split(",");
		var ds = document.getElementById("datasources");
		ds.options.length = 0;
		for( i = 0; i < tmp_ds.length; ++i)
		{
			element_temp = tmp_ds[i].split("|");
      			var item = document.createElement("option");
		        item.text = element_temp[1];
                        item.value = element_temp[0];
                        ds.options.add(item);
		}

                var tmp_sele = $.cookie("ATLASselectionlist").split("|");
		sele_elements = tmp_sele.length / 4;
		for( i = 0; i < sele_elements; ++i)
		{
			tmp_selement = []
			tmp_selement.push(tmp_sele[i*4]);
                        tmp_selement.push(tmp_sele[(i*4)+1]);
                        tmp_selement.push(tmp_sele[(i*4)+2]);
                        tmp_selement.push(tmp_sele[(i*4)+3]);
			selection_list.push(tmp_selement);
		}

		update_selection_table();
                update_json_source();


	}
}

function load_java_applet()
{
w=800;
h=600;
if (window.screen) {
w = window.screen.availWidth;
h = window.screen.availHeight;
}
window.open("java.jsp", '_blank', 'toolbar=0,location=0,menubar=0,width='+w+',height='+h);
}

function duplicate_window()
{
save_view_state();
w=800;
h=600;
if (window.screen) {
w = window.screen.availWidth;
h = window.screen.availHeight;
}
window.open("viewer.jsp?duplicate=1", '_blank', 'toolbar=0,location=0,menubar=0,width='+w+',height='+h);
}

function exportIMG()
{
      var img = $('#chart').jqplotToImage(50, 0);
      if (img) {
        open(img.toDataURL("image/png"));
      }  
}


function changeColor()
{
      var ds = document.getElementById("datasources");
      var cs = document.getElementById("ColorSource");
      if( !(ds.selectedIndex >= 0))
	      alert("Please select a source to change the color");
      else{
	      plot1.seriesColors[ds.selectedIndex] = cs.value;
	      refresh_graph();
              update_selection_graph();
      }


}


function LEIsHelp()
{
//        var $dialog = $('<div></div>')
//                .html('<p>Currently these are the definitions of the Ligand Efficiency Indices (LEI) used in this platform:</p> <CENTER><table width="350" align= "center" border="1" RULES="ROWS" FRAME="HSIDES" cellspacing="0" cellpadding="2" style="font-size: 0.8em;"> <tr> <td><p><strong>Name</strong></p></td>  <td><p><strong>Definition</strong></p></td>  </tr> <tr> <td><p>BEI</p></td> <td><p>p(Ki), p(Kd), or p(IC<sub>50</sub>)/MW(kiloDa) </p></td> </tr> <tr> <td><p>SEI</p></td><td><p>p(K<sub>i</sub>), p(K<sub>d</sub>), or p(IC<sub>50</sub>)/(PSA/100 &Aring;<sup>2</sup>)</p></td><tr><td><p>NSEI</p></td><td><p>NSEI = -log<sub>10</sub> Ki/(NPOL) = pKi/NPOL(N,O)</p></td><tr><td><p>NBEI</p></td> <td><p>NBEI= -log<sub>10</sub> Ki/(NHEA)= pKi/(NHEA)</p></td> </tr <tr><td><p>nBEI</p></td<td><p>nBEI= -log<sub>10</sub>[(Ki/NHEA)]</p></td></tr><tr><td><p>mBEI</p></td><td><p>mBEI=-log<sub>10</sub>[(Ki/MW)]</p></td> </tr></table></CENTER><p>More information can be found <A HREF="http://www.uic.edu/labs/caz/discovery/index.html">here</A></p>')


	var $dialog = $('<div></div>').html('<ul><li> LEH,BEI,NBEI,nBEI and mBEI are efficiency indices related to the size of the ligand.</li><li>SEI,NSEI,LEP are efficiency indices relating to the polarity of the ligand.</li></ul><hr><ul><li>BEI is the pKi scaled to the MW in KiloDaltons (KDa).</li><li>LE(or LEH) is RTlnKi divided by the number of non-hydrogen atoms (NHEA)(Hopkins). </li><li>NBEI is the pKi scaled to the number of non-hydrogen atoms (NHEA).</li><li>nBEI, mBEI the log<sub>10</sub> operation is taken after the ratio of K<sub>i</sub> to the scaling factor (NHEA, MW) respectively. This might appear to be odd but the algebra is such that the slope of the lines in these planes is easier to interpret and equal to NPOL.</li></ul> <hr> <ul><li>SEI is pK<sub>i</sub> scaled down to the Polar Surface Area (normalized to 100 A<sup>2</sup>).</li><li> NSEI is pK<sub>i</sub> divided by the number of polar atoms (N+O). </li><li> LEP is RTlnKi divided by NPOL(N+O):extension of LE (Hopkins) to Polar atoms (CAZ unpublished). </li><li> LEH vs LEP make an efficiency plane equivalent to NBEI vs NSEI, except for a constant factor. </li></ul><hr>Efficiency planes combine the two types of variables, typically BEI(size-group) vs. SEI(polarity-group) (y vs. x).<p>Variable names:</p><ul><li>NHEA (or NHA): number of non-hydrogen atoms in the ligand</li><li>PSA (or tPSA): Polar Surface Area (or topological Polar Surface area)</li><li>NPOL: number of Polar Atoms,  the count of N+O atoms in the ligand</li><li>MW: Molecular Weight, in the BEI definition should be in KiloDaltons</li></ul><p>More information can be found <A HREF="http://www.uic.edu/labs/caz/discovery/index.html">here</A></p>')
                .dialog({
                        autoOpen: true,
                        title: 'Ligand Efficiency Indices variables and clarifications',
                        resizable: true,
                        width: 700,
                        height:360,
                        modal: false
                });

}


function downloadCSV(){
window.open("LEIDownloadData?x="+ current_x +"&y="+ current_y + "&sources="+escape(sources_string));
}

$(function() {

  $.fn.jqplotToImage =
  function(x_offset, y_offset) {
    if ($(this).width() == 0 || $(this).height() == 0) {
      return null;
    }
    var newCanvas = document.createElement("canvas");
    newCanvas.width = $(this).outerWidth() + Number(x_offset);
    newCanvas.height = $(this).outerHeight() + Number(y_offset);

    if (!newCanvas.getContext) return null;

    var newContext = newCanvas.getContext("2d");
    newContext.textAlign = 'left';
    newContext.textBaseline = 'top';

    function _jqpToImage(el, x_offset, y_offset) {
      var tagname = el.tagName.toLowerCase();
      var p = $(el).position();
      var css = getComputedStyle(el);
      var left = x_offset + p.left + parseInt(css.marginLeft) + parseInt(css.borderLeftWidth) + parseInt(css.paddingLeft);
      var top = y_offset + p.top + parseInt(css.marginTop) + parseInt(css.borderTopWidth)+ parseInt(css.paddingTop);

      if ((tagname == 'div' || tagname == 'span') && !$(el).hasClass('jqplot-highlighter-tooltip')) {
        $(el).children().each(function() {
          _jqpToImage(this, left, top);
        });
        var text = $(el).childText();

        if (text) {
          var metrics = newContext.measureText(text);
          newContext.font = $(el).getComputedFontStyle();
          newContext.fillText(text, left, top);
          // For debugging.
          //newContext.strokeRect(left, top, $(el).width(), $(el).height());
        }
      }
      else if (tagname == 'canvas') {
        newContext.drawImage(el, left, top);
      }
    }
    $(this).children().each(function() {
      _jqpToImage(this, x_offset, y_offset);
    });
    return newCanvas;
  };

  $.fn.css2 = jQuery.fn.css;
  $.fn.css = function() {
    if (arguments.length) return jQuery.fn.css2.apply(this, arguments);
    return window.getComputedStyle(this[0]);
  };

  // Returns font style as abbreviation for "font" property.
  $.fn.getComputedFontStyle = function() {
    var css = this.css();
    var attr = ['font-style', 'font-weight', 'font-size', 'font-family'];
    var style = [];

    for (var i=0 ; i < attr.length; ++i) {
      var attr = String(css[attr[i]]);

      if (attr && attr != 'normal') {
        style.push(attr);
      }
    }
    return style.join(' ');
  }

  $.fn.childText =
    function() {
      return $(this).contents().filter(function() {
        return this.nodeType == 3; // Node.TEXT_NODE not defined in I7
      }).text();
    };

});


