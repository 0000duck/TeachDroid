/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*    Auftragsnr: 5500395
*    Erstautor : ede
*    Datum     : 01.04.2003
*--------------------------------------------------------------------------
*      Revision:
*        Author:
*          Date:
*------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.edit;


/**
* Diese Klasse enthält eine Sammlung mit allen Schlüsselwörtern die für die
* Genierung von Sourcen wichtig sind.
*/
public class KEditKW {

   public final static String KW_BLANK = " ";
   public final static String KW_TAB = "  ";
   public static String KW_NEWLINE = "\n";

   public final static String KW_INHERIT           = "INHERIT";
   public final static String KW_SEMICOLON         = ";";
   public final static String KW_PRIVATE           = "PRIVATE";
   public final static String KW_GLOBAL            = "GLOBAL";
   public final static String KW_CONST             = "CONST";
   public final static String KW_VALUE_PARAM       = "VAR_IN";
   public final static String KW_SAVE              = "SAVE";
   public final static String KW_END_SAVE          = "END_SAVE";
   public final static String KW_COLON_EQUAL       = ":=";
   public final static String KW_CONSTANT          = "CONSTANT";
   public final static String KW_END_CONSTANT      = "END_CONSTANT";
   public final static String KW_COLON             = ":";
   public final static String KW_VAR               = "VAR";
   public final static String KW_END_VAR           = "END_VAR";
   public final static String KW_TYPE              = "TYPE";
   public final static String KW_END_TYPE          = "END_TYPE";
   public final static String KW_MAPTO             = "MAPTO";
   public final static String KW_ROUTINE           = "ROUTINE";
   public final static String KW_PARENTHESES_LEFT  = "(";
   public final static String KW_PARENTHESES_RIGHT = ")";
   public final static String KW_BRACKET_LEFT      = "[";
   public final static String KW_BRACKET_RIGHT     = "]";
   public final static String KW_DOUBLE_POINT      = "..";
   public final static String KW_COMMA             = ",";
   public final static String KW_ARRAY             = "ARRAY";
   public final static String KW_OF                = "OF";
   public final static String KW_STRUCT            = "STRUCT";
   public final static String KW_END_STRUCT        = "END_STRUCT";
   public final static String KW_CASE              = "CASE";
   public final static String KW_END_ROUTINE       = "END_ROUTINE";
   public final static String KW_AT                = "AT";
   public final static String KW_START             = "START";
   public final static String KW_PRIORITY          = "PRIO";
   public final static String KW_DO                = "DO";
   public final static String KW_STOP              = "STOP";
   public final static String KW_IF                = "IF";
   public final static String KW_THEN              = "THEN";
   public final static String KW_END_IF            = "END_IF";
   public final static String KW_ELSE_IF           = "ELSIF";
   public final static String KW_ELSE              = "ELSE";
   public final static String KW_GOTO              = "GOTO";
   public final static String KW_END_CASE          = "END_CASE";
   public final static String KW_FOR               = "FOR";
   public final static String KW_TO                = "TO";
   public final static String KW_END_FOR           = "END_FOR";
   public final static String KW_WHILE             = "WHILE";
   public final static String KW_END_WHILE         = "END_WHILE";
   public final static String KW_REPEAT            = "REPEAT";
   public final static String KW_UNTIL             = "UNTIL";
   public final static String KW_END_REPEAT        = "END_REPEAT";
   public final static String KW_LABEL             = "LABEL";
   public final static String KW_SELECT            = "SELECT";
   public final static String KW_DESELECT          = "DESELECT";
   public final static String KW_RETURN            = "RETURN";
   public final static String KW_WAIT              = "WAIT";
   public final static String KW_POINT             = ".";
   public final static String KW_LESS              = "<";
   public final static String KW_GREATER           = ">";
   public final static String KW_LESS_EQUAL        = "<=";
   public final static String KW_GREATER_EQUAL     = ">=";
   public final static String KW_EQUAL             = "=";
   public final static String KW_NOT_EQUAL         = "<>";
   public final static String KW_MINUS             = "-";
   public final static String KW_PLUS              = "+";
   public final static String KW_OR                = "OR";
   public final static String KW_XOR               = "XOR";
   public final static String KW_MUL               = "*";
   public final static String KW_DIV               = "/";
   public final static String KW_MOD               = "MOD";
   public final static String KW_AND               = "AND";
   public final static String KW_NOT               = "NOT";
   public final static String KW_LOCK              = "LOCK";
   public final static String KW_UNLOCK            = "UNLOCK";
   public final static String KW_USER              = "USER";
   public final static String KW_LOOP              = "LOOP";
   public final static String KW_END_LOOP          = "END_LOOP";
   public final static String KW_CALL              = "CALL";
   public final static String KW_RUN              = "RUN";
   public final static String KW_KILL              = "KILL";
   public final static String KW_OPTIONAL          = "OPT";

   
   public final static String KW_QUOTATION_MARKS = "\"";
   public final static String KW_TRUE = "TRUE";
   public final static String KW_FALSE = "FALSE";
   public final static String KW_SYSVAR = "SYSVAR";
   public final static String KW_SYSEVENT = "SYSEVENT";
   public final static String KW_PRINT = "PRINT";
   public final static String KW_ABS = "ABS";
   public final static String KW_RND = "RND";
   public final static String KW_MAP = "MAP";
   public final static String KW_BOOL = "BOOL";
   public final static String KW_DINT = "DINT";
   public final static String KW_INT = "INT";
   public final static String KW_LINT = "LINT";
   public final static String KW_SINT = "SINT";
   public final static String KW_BYTE = "BYTE";
   public final static String KW_WORD = "WORD";
   public final static String KW_DWORD = "DWORD";
   public final static String KW_LWORD = "LWORD";
   public final static String KW_REAL = "REAL";
   public final static String KW_STRING = "STRING";
   public final static String KW_VARIANT = "VARIANT";
   public final static String KW_ENUMERATION = "ENUMERATION";
   public final static String KW_STRUCTURE = "STRUCTURE";
   public final static String KW_SUBRANGE = "SUBRANGE";
   
   public final static String KW_LREAL = "LREAL";

   public static String ROUTINE_RESERVE = "RESERVE";
   public static String ROUTINE_CONTINUE = "CONTINUE";
   public static String ROUTINE_RELEASE = "RELEASE";
   public static String ROUTINE_INTERRUPT = "INTERRUPT";
   public static String ROUTINE_NEW = "NEW";
   public static String ROUTINE_DELETE = "DELETE";

   public static String[] SYSTEM_ROUTINES = { ROUTINE_RESERVE, ROUTINE_CONTINUE, ROUTINE_RELEASE, ROUTINE_INTERRUPT };
   public static String[] SYSTEM_ROUTINES_ALL =
      { ROUTINE_RESERVE, ROUTINE_CONTINUE, ROUTINE_RELEASE, ROUTINE_INTERRUPT, ROUTINE_NEW, ROUTINE_DELETE };
   public static String[] SYSTEM_ROUTINES_WITHOUT_PARAMS = { ROUTINE_RESERVE, ROUTINE_CONTINUE, ROUTINE_RELEASE, ROUTINE_INTERRUPT, ROUTINE_DELETE };

   /** Array mit allen Terminal Schlüsselwörtern */
   public static String[] keyWords = new String[100];

   static {
      try {
         KW_NEWLINE = System.getProperty("line.separator");
      } catch (Exception e) {
         KW_NEWLINE = "\n";
      }
      keyWords[0] = KW_BOOL;
      keyWords[1] = KW_DINT;
      keyWords[2] = KW_REAL;
      keyWords[3] = KW_STRING;
      keyWords[4] = KW_VARIANT;
      keyWords[5] = KW_ENUMERATION;
      keyWords[6] = KW_STRUCTURE;
      keyWords[7] = KW_SUBRANGE;
     
      // initial terminal symbol keywords  
      keyWords[8] = KW_INHERIT;
      keyWords[9] = KW_SEMICOLON;
      keyWords[10] = KW_PRIVATE;
      keyWords[11] = KW_GLOBAL;
      keyWords[12] = KW_CONST;
      keyWords[13] = KW_VALUE_PARAM;
      keyWords[14] = KW_SAVE;
      keyWords[15] = KW_END_SAVE;
      keyWords[16] = KW_COLON_EQUAL;
      keyWords[17] = KW_CONSTANT;
      keyWords[18] = KW_END_CONSTANT;
      keyWords[19] = KW_COLON;
      keyWords[20] = KW_VAR;
      keyWords[21] = KW_END_VAR;
      keyWords[22] = KW_TYPE;
      keyWords[23] = KW_END_TYPE;
      keyWords[24] = KW_MAPTO;
      keyWords[25] = KW_ROUTINE;
      keyWords[26] = KW_PARENTHESES_LEFT;
      keyWords[27] = KW_PARENTHESES_RIGHT;
      keyWords[28] = KW_BRACKET_LEFT;
      keyWords[29] = KW_BRACKET_RIGHT;
      keyWords[30] = KW_DOUBLE_POINT;
      keyWords[31] = KW_COMMA;
      keyWords[32] = KW_ARRAY;
      keyWords[33] = KW_OF;
      keyWords[34] = KW_STRUCT;
      keyWords[35] = KW_END_STRUCT;
      keyWords[36] = KW_CASE;
      keyWords[37] = KW_END_ROUTINE;
      keyWords[38] = KW_AT;
      keyWords[39] = KW_START;
      keyWords[40] = KW_PRIORITY;
      keyWords[41] = KW_DO;
      keyWords[42] = KW_STOP;
      keyWords[43] = KW_IF;
      keyWords[44] = KW_THEN;
      keyWords[45] = KW_END_IF;
      keyWords[46] = KW_ELSE_IF;
      keyWords[47] = KW_ELSE;
      keyWords[48] = KW_GOTO;
      keyWords[49] = KW_END_CASE;
      keyWords[50] = KW_FOR;
      keyWords[51] = KW_TO;
      keyWords[52] = KW_END_FOR;
      keyWords[53] = KW_WHILE;
      keyWords[54] = KW_END_WHILE;
      keyWords[55] = KW_REPEAT;
      keyWords[56] = KW_UNTIL;
      keyWords[57] = KW_END_REPEAT;
      keyWords[58] = KW_LABEL;
      keyWords[59] = KW_SELECT;
      keyWords[60] = KW_DESELECT;
      keyWords[61] = KW_RETURN;
      keyWords[62] = KW_WAIT;
      keyWords[63] = KW_POINT;
      keyWords[64] = KW_LESS;
      keyWords[65] = KW_GREATER;
      keyWords[66] = KW_LESS_EQUAL;
      keyWords[67] = KW_GREATER_EQUAL;
      keyWords[68] = KW_EQUAL;
      keyWords[69] = KW_NOT_EQUAL;
      keyWords[70] = KW_MINUS;
      keyWords[71] = KW_PLUS;
      keyWords[72] = KW_OR;
      keyWords[73] = KW_XOR;
      keyWords[74] = KW_MUL;
      keyWords[75] = KW_DIV;
      keyWords[76] = KW_MOD;
      keyWords[77] = KW_AND;
      keyWords[78] = KW_NOT;
      keyWords[79] = KW_LOCK;
      keyWords[80] = KW_UNLOCK;
      keyWords[81] = KW_USER;
      keyWords[82] = KW_LOOP;
      keyWords[83] = KW_END_LOOP;
      keyWords[84] = KW_CALL;
      keyWords[85] = KW_OPTIONAL;
      keyWords[86] = KW_RUN;
      keyWords[87] = KW_KILL;
   }

   public static boolean isReserved(String identifier, boolean isCaseSensitive) {
      if ((identifier == null) || (identifier.length() <= 0)) {
         return false;
      }
      String newIdent;
      if (isCaseSensitive) {
         newIdent = identifier;
      } else {
         newIdent = identifier.toUpperCase();
      }
      for (int i = 0; i < KEditKW.keyWords.length; i++) {
         if (newIdent.equals(KEditKW.keyWords[i])) {
            return true;
         }
      }
      for (int i = 0; i < SYSTEM_ROUTINES_ALL.length; i++) {
         if (newIdent.equals(SYSTEM_ROUTINES_ALL[i])) {
            return true;
         }
      }
      return false;
   }
}
