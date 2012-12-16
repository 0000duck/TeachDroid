package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcSymbolKind implements SDR {
   /** Int */
   public static final int rpcTComment = -2;
   /** Int */
   public static final int rpcTError = -1;
   /** Int */
   public static final int rpcTEof = 0;
   /** Int */
   public static final int rpcTInherit = 1;
   /** Int */
   public static final int rpcTSemicolon = 2;
   /** Int */
   public static final int rpcTPrivate = 3;
   /** Int */
   public static final int rpcTGlobal = 4;
   /** Int */
   public static final int rpcTConst = 5;
   /** Int */
   public static final int rpcTValueParam = 6;
   /** Int */
   public static final int rpcTSave = 7;
   /** Int */
   public static final int rpcTEndSave = 8;
   /** Int */
   public static final int rpcTColonEqual = 9;
   /** Int */
   public static final int rpcTConstant = 10;
   /** Int */
   public static final int rpcTEndConstant = 11;
   /** Int */
   public static final int rpcTColon = 12;
   /** Int */
   public static final int rpcTVar = 13;
   /** Int */
   public static final int rpcTEndVar = 14;
   /** Int */
   public static final int rpcTType = 15;
   /** Int */
   public static final int rpcTEndType = 16;
   /** Int */
   public static final int rpcTMapTo = 17;
   /** Int */
   public static final int rpcTRoutine = 18;
   /** Int */
   public static final int rpcTParenthesesLeft = 19;
   /** Int */
   public static final int rpcTParenthesesRight = 20;
   /** Int */
   public static final int rpcTBracketLeft = 21;
   /** Int */
   public static final int rpcTBracketRight = 22;
   /** Int */
   public static final int rpcTDoublePoint = 23;
   /** Int */
   public static final int rpcTComma = 24;
   /** Int */
   public static final int rpcTArray = 25;
   /** Int */
   public static final int rpcTOf = 26;
   /** Int */
   public static final int rpcTStruct = 27;
   /** Int */
   public static final int rpcTEndStruct = 28;
   /** Int */
   public static final int rpcTCase = 29;
   /** Int */
   public static final int rpcTEndRoutine = 30;
   /** Int */
   public static final int rpcTAt = 31;
   /** Int */
   public static final int rpcTStart = 32;
   /** Int */
   public static final int rpcTPrio = 33;
   /** Int */
   public static final int rpcTDo = 34;
   /** Int */
   public static final int rpcTStop = 35;
   /** Int */
   public static final int rpcTIf = 36;
   /** Int */
   public static final int rpcTThen = 37;
   /** Int */
   public static final int rpcTEndIf = 38;
   /** Int */
   public static final int rpcTElseIf = 39;
   /** Int */
   public static final int rpcTElse = 40;
   /** Int */
   public static final int rpcTGoto = 41;
   /** Int */
   public static final int rpcTEndCase = 42;
   /** Int */
   public static final int rpcTFor = 43;
   /** Int */
   public static final int rpcTTo = 44;
   /** Int */
   public static final int rpcTEndFor = 45;
   /** Int */
   public static final int rpcTWhile = 46;
   /** Int */
   public static final int rpcTEndWhile = 47;
   /** Int */
   public static final int rpcTRepeat = 48;
   /** Int */
   public static final int rpcTUntil = 49;
   /** Int */
   public static final int rpcTEndRepeat = 50;
   /** Int */
   public static final int rpcTLabel = 51;
   /** Int */
   public static final int rpcTSelect = 52;
   /** Int */
   public static final int rpcTDeselect = 53;
   /** Int */
   public static final int rpcTReturn = 54;
   /** Int */
   public static final int rpcTWait = 55;
   /** Int */
   public static final int rpcTPoint = 56;
   /** Int */
   public static final int rpcTLess = 57;
   /** Int */
   public static final int rpcTGreater = 58;
   /** Int */
   public static final int rpcTLessEqual = 59;
   /** Int */
   public static final int rpcTGreaterEqual = 60;
   /** Int */
   public static final int rpcTEqual = 61;
   /** Int */
   public static final int rpcTNotEqual = 62;
   /** Int */
   public static final int rpcTMinus = 63;
   /** Int */
   public static final int rpcTPlus = 64;
   /** Int */
   public static final int rpcTOr = 65;
   /** Int */
   public static final int rpcTXor = 66;
   /** Int */
   public static final int rpcTMul = 67;
   /** Int */
   public static final int rpcTDiv = 68;
   /** Int */
   public static final int rpcTMod = 69;
   /** Int */
   public static final int rpcTAnd = 70;
   /** Int */
   public static final int rpcTNot = 71;
   /** Int */
   public static final int rpcTIdent = 72;
   /** Int */
   public static final int rpcTInteger = 73;
   /** Int */
   public static final int rpcTReal = 74;
   /** Int */
   public static final int rpcTString = 75;
   /** Int */
   public static final int rpcTLock = 76;
   /** Int */
   public static final int rpcTUnlock = 77;
   /** Int */
   public static final int rpcTNewLine = 78;
   /** Int */
   public static final int rpcTUser = 79;
   /** Int */
   public static final int rpcTLoop = 80;
   /** Int */
   public static final int rpcTEndLoop = 81;
   /** Int */
   public static final int rpcTCall = 82;
   /** Int */
   public static final int rpcTOptional = 83;
   /** Int */
   public static final int rpcTProject = 84;
   /** Int */
   public static final int rpcTCurlyBracketLeft = 85;
   /** Int */
   public static final int rpcTCurlyBracketRight = 86;
   /** Int */
   public static final int rpcTAttrSeperator = 87;
   /** Int */
   public static final int rpcTName = 88;
   /** Int */
   public static final int rpcTReadonly = 89;
   /** Int */
   public static final int rpcTRun = 90;
   /** Int */
   public static final int rpcTKill = 91;
   /** Int */
   public static final int rpcTAbstract = 92;
   /** Int */
   public static final int rpcTDeactivate = 93;
   /** Int */
   public static final int rpcTBegin = 94;
   /** Int */
   public static final int rpcTEnd = 95;
   /** Int */
   public static final int rpcTDeprecated = 96;
   /** Int */
   public static final int rpcNTConstPart = 101;
   /** Int */
   public static final int rpcNTVarPart = 102;
   /** Int */
   public static final int rpcNTTypePart = 103;
   /** Int */
   public static final int rpcNTVarDeclaration = 104;
   /** Int */
   public static final int rpcNTTypeDefinition = 105;
   /** Int */
   public static final int rpcNTConstDeclaration = 106;
   /** Int */
   public static final int rpcNTStatementPart = 107;
   /** Int */
   public static final int rpcNTParameter = 108;
   /** Int */
   public static final int rpcNTEnumeration = 109;
   /** Int */
   public static final int rpcNTEnumerationType = 110;
   /** Int */
   public static final int rpcNTStructType = 111;
   /** Int */
   public static final int rpcNTArrayType = 112;
   /** Int */
   public static final int rpcNTFunctionBlock = 120;
   /** Int */
   public static final int rpcNTAtBlock = 121;
   /** Int */
   public static final int rpcNTHead = 122;
   /** Int */
   public static final int rpcNTAtHead = 123;
   /** Int */
   public static final int rpcNTAttribBlock = 124;
   /** Int */
   public static final int rpcNTEmptyStatement = 130;
   /** Int */
   public static final int rpcNTAssignStatement = 131;
   /** Int */
   public static final int rpcNTCallStatement = 132;
   /** Int */
   public static final int rpcNTWaitStatement = 133;
   /** Int */
   public static final int rpcNTIfStatement = 134;
   /** Int */
   public static final int rpcNTIfGotoStatement = 135;
   /** Int */
   public static final int rpcNTCaseStatement = 136;
   /** Int */
   public static final int rpcNTForStatement = 137;
   /** Int */
   public static final int rpcNTWhileStatement = 138;
   /** Int */
   public static final int rpcNTRepeatStatement = 139;
   /** Int */
   public static final int rpcNTReturnStatement = 140;
   /** Int */
   public static final int rpcNTLabelStatement = 141;
   /** Int */
   public static final int rpcNTGotoStatement = 142;
   /** Int */
   public static final int rpcNTSelectStatement = 144;
   /** Int */
   public static final int rpcNTDeselectStatement = 145;
   /** Int */
   public static final int rpcNTStopStatement = 146;
   /** Int */
   public static final int rpcNTLockStatement = 147;
   /** Int */
   public static final int rpcNTUnlockStatement = 148;
   /** Int */
   public static final int rpcNTLoopStatement = 149;
   /** Int */
   public static final int rpcNTCaseSel = 158;
   /** Int */
   public static final int rpcNTRoutine = 159;
   /** Int */
   public static final int rpcNTExpression = 160;

   /** Int */
   public int value;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcSymbolKind() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      if (mMemberDone == 0) {
         value = in.readInt(context);
         if (context.done)
            mMemberDone++;
      }
   }
   public void write(SDROutputStream out, SDRContext context) throws SDRException, IOException {
      if (mMemberDone == 0) {
         out.writeInt(value, context);
         if (context.done)
            mMemberDone++;
      }
   }
   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(value);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
