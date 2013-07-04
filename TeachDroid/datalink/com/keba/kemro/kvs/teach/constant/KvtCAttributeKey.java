/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : KEMRO.teachview.4
 *    Auftragsnr: 5500395
 *    Erstautor : sinn
 *    Datum     : 01.04.2003
 *--------------------------------------------------------------------------
 *      Revision:
 *        Author:  sinn
 *          Date:
 *------------------------------------------------------------------------*/
package com.keba.kemro.kvs.teach.constant;


/**
 * Enthält die Deklaration der Namen der Standardattribute für die Strukturbaumknoten.
 */
public class KvtCAttributeKey {
	/** automatic start of the program */
	public final static String	D_PROGRAM_AUTO_START			= "auto";

	public final static String	D_INIT_VALUE					= "init";

	public final static String	D_SHOW_INSTANCE					= "show_instance";
	/**
	 * manual start of the program is forbidden
	 */
	public final static String	D_PROGRAM_NO_MANUAL_START		= "no_manual";

	/**
	 * manual start of the program is forbidden
	 */
	public final static String	D_PROGRAM_EXTERNALLY_VISIBLE	= "externally_visible";
	/**
	 * macro key is used for routines and units which should be shown in macro selection mask.
	 */
	public final static String	D_MAKRO							= "macro";

	/**
	 * category key is used for types which should be shown in new variable creation mask.
	 */
	public final static String	D_CATEGORY						= "category";

	/**
	 * color key is used for routines. It defines the color for the macro shown in the program mask.
	 */
	public final static String	D_COLOR							= "color";
	/**
	 * reuse key is used for variables which should be reused instead allocating a new one.
	 */
	public static final String	REUSE							= "reuse";
	/**
	 * no_var key is used for types. It isn't allowed to allocate a variables from this type.
	 */
	public static final String	NOVAR							= "no_var";
	/**
	 * default key is used for unit types which are derived form the same base unit. Only for one
	 * derived unit is it allowed to have this attribute.
	 */
	public static final String	DEFAULT							= "default";
	/**
	 * type key specifies for a parameter that only variables of the given types should be selectable.
	 */
	public static final String	TYPE							= "type";
	/**
	 * prefix key is used for types. It means that the name of the allocated variables starts with
	 * this prefix followed of increasing nummber.
	 */
	public static final String	PREFIX							= "prefix";
	/**
	 * showref key is used for variables. It means that the mapto reference should be shown.
	 */
	public static final String	SHOW_REF						= "showref";

	/**
	 * showref key is used for variables. It means that the mapto reference should be shown.
	 */
	public static final String	SHOW_EMPTY_REF					= "showemptyref";
	/**
	 * hide key is used for unit variables. It means that this node will not be shown.
	 */
	public static final String	HIDE							= "hide";

	/**
	 * enable key is used for unit variables. It means that this node will not be shown in macro selection mask.
	 */
	public static final String	ENABLE							= "enable";

	public static final String	PERSISTENT						= "persistent";

	public static final String	DO_ALLOWED						= "do_allowed";

	/**
	 * xmlmask specifies a xml user mask for unit variables
	 */
	public static final String	XMLMASK							= "xmlmask";

	/**
	 * does not expand variable members in variable tree
	 */
	public static final String	NO_EXPAND						= "noexpand";

	/**
	 * xmlmaskvar specifies a mapto variable which is mapped when the corresponding xmlmask
	 * opened.
	 */
	public static final String	XMLMASKVAR						= "xmlmaskvar";

	/**
	 * Components of units can be exportet for the usage in the varchooser
	 * eg: exportcomponent="BOOLEAN:state;DINT:y" means the state component is preseted in the var chooser
	 */
	public static final String	EXPORTCOMPONENT					= "exportcomponent";

	/**
	 * if macro is teachable the teachbutton is only avaiable at this member
	 */

	public static final String	TEACHMEMBER						= "teachmember";

	/**
	 * This specified programlocal variable is used to enter numeric values in the combobox
	 */
	public static final String	COMBOBOX_VALUE					= "comboboxvalue";

	/**
	 * Extract arrayfields if specified
	 */
	public static final String	EXPAND_FIELDS					= "expandfields";

	/**
	 * Default scope for variables. This overrides the current scope in the New-Var mask {@link KvtNewVarController}.
	 */
	public static final String	DEFAULT_SCOPE					= "defaultScope";

}
