// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g 2013-08-23 16:21:03

    package org.apache.cassandra.cql3;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.HashMap;
    import java.util.LinkedHashMap;
    import java.util.List;
    import java.util.Map;

    import org.apache.cassandra.cql3.statements.*;
    import org.apache.cassandra.utils.Pair;
    import org.apache.cassandra.thrift.ConsistencyLevel;
    import org.apache.cassandra.thrift.InvalidRequestException;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CqlParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "K_USE", "K_SELECT", "K_COUNT", "K_FROM", "K_USING", "K_CONSISTENCY", "K_LEVEL", "K_WHERE", "K_ORDER", "K_BY", "K_LIMIT", "INTEGER", "K_WRITETIME", "K_TTL", "K_AND", "K_ASC", "K_DESC", "K_INSERT", "K_INTO", "K_VALUES", "K_TIMESTAMP", "K_UPDATE", "K_SET", "K_DELETE", "K_BEGIN", "K_BATCH", "K_APPLY", "K_CREATE", "K_KEYSPACE", "K_WITH", "K_COLUMNFAMILY", "K_PRIMARY", "K_KEY", "K_COMPACT", "K_STORAGE", "K_CLUSTERING", "K_INDEX", "IDENT", "K_ON", "K_ALTER", "K_TYPE", "K_ADD", "K_DROP", "K_TRUNCATE", "QUOTED_NAME", "K_TOKEN", "STRING_LITERAL", "UUID", "FLOAT", "QMARK", "K_IN", "K_ASCII", "K_BIGINT", "K_BLOB", "K_BOOLEAN", "K_COUNTER", "K_DECIMAL", "K_DOUBLE", "K_FLOAT", "K_INT", "K_TEXT", "K_UUID", "K_VARCHAR", "K_VARINT", "K_TIMEUUID", "S", "E", "L", "C", "T", "F", "R", "O", "M", "W", "H", "A", "N", "D", "K", "Y", "I", "U", "P", "G", "Q", "B", "X", "V", "J", "Z", "DIGIT", "LETTER", "HEX", "WS", "COMMENT", "MULTILINE_COMMENT", "';'", "'('", "')'", "','", "'\\*'", "'='", "'.'", "'+'", "'-'", "':'", "'<'", "'<='", "'>='", "'>'"
    };
    public static final int LETTER=96;
    public static final int K_INT=63;
    public static final int K_CREATE=31;
    public static final int K_CLUSTERING=39;
    public static final int K_WRITETIME=16;
    public static final int EOF=-1;
    public static final int K_PRIMARY=35;
    public static final int K_VALUES=23;
    public static final int K_USE=4;
    public static final int STRING_LITERAL=50;
    public static final int K_ON=42;
    public static final int K_USING=8;
    public static final int K_ADD=45;
    public static final int K_ASC=19;
    public static final int K_KEY=36;
    public static final int COMMENT=99;
    public static final int K_TRUNCATE=47;
    public static final int K_ORDER=12;
    public static final int D=82;
    public static final int E=70;
    public static final int F=74;
    public static final int G=88;
    public static final int K_COUNT=6;
    public static final int K_KEYSPACE=32;
    public static final int K_TYPE=44;
    public static final int A=80;
    public static final int B=90;
    public static final int C=72;
    public static final int L=71;
    public static final int M=77;
    public static final int N=81;
    public static final int O=76;
    public static final int H=79;
    public static final int I=85;
    public static final int J=93;
    public static final int K_UPDATE=25;
    public static final int K=83;
    public static final int U=86;
    public static final int T=73;
    public static final int W=78;
    public static final int K_TEXT=64;
    public static final int V=92;
    public static final int Q=89;
    public static final int P=87;
    public static final int K_COMPACT=37;
    public static final int S=69;
    public static final int R=75;
    public static final int K_TTL=17;
    public static final int Y=84;
    public static final int X=91;
    public static final int Z=94;
    public static final int K_INDEX=40;
    public static final int K_INSERT=21;
    public static final int WS=98;
    public static final int K_APPLY=30;
    public static final int K_STORAGE=38;
    public static final int K_TIMESTAMP=24;
    public static final int K_AND=18;
    public static final int K_DESC=20;
    public static final int K_TOKEN=49;
    public static final int QMARK=53;
    public static final int K_LEVEL=10;
    public static final int K_BATCH=29;
    public static final int K_UUID=65;
    public static final int K_ASCII=55;
    public static final int UUID=51;
    public static final int K_DELETE=27;
    public static final int T__114=114;
    public static final int K_BY=13;
    public static final int FLOAT=52;
    public static final int K_FLOAT=62;
    public static final int K_VARINT=67;
    public static final int K_DOUBLE=61;
    public static final int K_SELECT=5;
    public static final int K_LIMIT=14;
    public static final int K_ALTER=43;
    public static final int K_BOOLEAN=58;
    public static final int K_SET=26;
    public static final int K_WHERE=11;
    public static final int QUOTED_NAME=48;
    public static final int MULTILINE_COMMENT=100;
    public static final int K_BLOB=57;
    public static final int T__107=107;
    public static final int HEX=97;
    public static final int K_INTO=22;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int T__103=103;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__111=111;
    public static final int T__110=110;
    public static final int K_VARCHAR=66;
    public static final int T__113=113;
    public static final int IDENT=41;
    public static final int T__112=112;
    public static final int DIGIT=95;
    public static final int K_BEGIN=28;
    public static final int INTEGER=15;
    public static final int K_COUNTER=59;
    public static final int K_DECIMAL=60;
    public static final int K_CONSISTENCY=9;
    public static final int K_WITH=33;
    public static final int T__102=102;
    public static final int T__101=101;
    public static final int K_IN=54;
    public static final int K_FROM=7;
    public static final int K_COLUMNFAMILY=34;
    public static final int K_DROP=46;
    public static final int K_BIGINT=56;
    public static final int K_TIMEUUID=68;

    // delegates
    // delegators


        public CqlParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public CqlParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return CqlParser.tokenNames; }
    public String getGrammarFileName() { return "/home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g"; }


        private List<String> recognitionErrors = new ArrayList<String>();
        private int currentBindMarkerIdx = -1;

        public void displayRecognitionError(String[] tokenNames, RecognitionException e)
        {
            String hdr = getErrorHeader(e);
            String msg = getErrorMessage(e, tokenNames);
            recognitionErrors.add(hdr + " " + msg);
        }

        public void addRecognitionError(String msg)
        {
            recognitionErrors.add(msg);
        }

        public List<String> getRecognitionErrors()
        {
            return recognitionErrors;
        }

        public void throwLastRecognitionError() throws InvalidRequestException
        {
            if (recognitionErrors.size() > 0)
                throw new InvalidRequestException(recognitionErrors.get((recognitionErrors.size()-1)));
        }

        // used by UPDATE of the counter columns to validate if '-' was supplied by user
        public void validateMinusSupplied(Object op, final Term value, IntStream stream) throws MissingTokenException
        {
            if (op == null && (value.isBindMarker() || Long.parseLong(value.getText()) > 0))
                throw new MissingTokenException(102, stream, value);
        }



    // $ANTLR start "query"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:121:1: query returns [ParsedStatement stmnt] : st= cqlStatement ( ';' )* EOF ;
    public final ParsedStatement query() throws RecognitionException {
        ParsedStatement stmnt = null;

        ParsedStatement st = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:124:5: (st= cqlStatement ( ';' )* EOF )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:124:7: st= cqlStatement ( ';' )* EOF
            {
            pushFollow(FOLLOW_cqlStatement_in_query72);
            st=cqlStatement();

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:124:23: ( ';' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==101) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:124:24: ';'
            	    {
            	    match(input,101,FOLLOW_101_in_query75); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match(input,EOF,FOLLOW_EOF_in_query79); 
             stmnt = st; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return stmnt;
    }
    // $ANTLR end "query"


    // $ANTLR start "cqlStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:127:1: cqlStatement returns [ParsedStatement stmt] : (st1= selectStatement | st2= insertStatement | st3= updateStatement | st4= batchStatement | st5= deleteStatement | st6= useStatement | st7= truncateStatement | st8= createKeyspaceStatement | st9= createColumnFamilyStatement | st10= createIndexStatement | st11= dropKeyspaceStatement | st12= dropColumnFamilyStatement | st13= dropIndexStatement | st14= alterTableStatement );
    public final ParsedStatement cqlStatement() throws RecognitionException {
        ParsedStatement stmt = null;

        SelectStatement.RawStatement st1 = null;

        UpdateStatement st2 = null;

        UpdateStatement st3 = null;

        BatchStatement st4 = null;

        DeleteStatement st5 = null;

        UseStatement st6 = null;

        TruncateStatement st7 = null;

        CreateKeyspaceStatement st8 = null;

        CreateColumnFamilyStatement.RawStatement st9 = null;

        CreateIndexStatement st10 = null;

        DropKeyspaceStatement st11 = null;

        DropColumnFamilyStatement st12 = null;

        DropIndexStatement st13 = null;

        AlterTableStatement st14 = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:129:5: (st1= selectStatement | st2= insertStatement | st3= updateStatement | st4= batchStatement | st5= deleteStatement | st6= useStatement | st7= truncateStatement | st8= createKeyspaceStatement | st9= createColumnFamilyStatement | st10= createIndexStatement | st11= dropKeyspaceStatement | st12= dropColumnFamilyStatement | st13= dropIndexStatement | st14= alterTableStatement )
            int alt2=14;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:129:7: st1= selectStatement
                    {
                    pushFollow(FOLLOW_selectStatement_in_cqlStatement113);
                    st1=selectStatement();

                    state._fsp--;

                     stmt = st1; 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:130:7: st2= insertStatement
                    {
                    pushFollow(FOLLOW_insertStatement_in_cqlStatement138);
                    st2=insertStatement();

                    state._fsp--;

                     stmt = st2; 

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:131:7: st3= updateStatement
                    {
                    pushFollow(FOLLOW_updateStatement_in_cqlStatement163);
                    st3=updateStatement();

                    state._fsp--;

                     stmt = st3; 

                    }
                    break;
                case 4 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:132:7: st4= batchStatement
                    {
                    pushFollow(FOLLOW_batchStatement_in_cqlStatement188);
                    st4=batchStatement();

                    state._fsp--;

                     stmt = st4; 

                    }
                    break;
                case 5 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:133:7: st5= deleteStatement
                    {
                    pushFollow(FOLLOW_deleteStatement_in_cqlStatement214);
                    st5=deleteStatement();

                    state._fsp--;

                     stmt = st5; 

                    }
                    break;
                case 6 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:134:7: st6= useStatement
                    {
                    pushFollow(FOLLOW_useStatement_in_cqlStatement239);
                    st6=useStatement();

                    state._fsp--;

                     stmt = st6; 

                    }
                    break;
                case 7 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:135:7: st7= truncateStatement
                    {
                    pushFollow(FOLLOW_truncateStatement_in_cqlStatement267);
                    st7=truncateStatement();

                    state._fsp--;

                     stmt = st7; 

                    }
                    break;
                case 8 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:136:7: st8= createKeyspaceStatement
                    {
                    pushFollow(FOLLOW_createKeyspaceStatement_in_cqlStatement290);
                    st8=createKeyspaceStatement();

                    state._fsp--;

                     stmt = st8; 

                    }
                    break;
                case 9 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:137:7: st9= createColumnFamilyStatement
                    {
                    pushFollow(FOLLOW_createColumnFamilyStatement_in_cqlStatement307);
                    st9=createColumnFamilyStatement();

                    state._fsp--;

                     stmt = st9; 

                    }
                    break;
                case 10 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:138:7: st10= createIndexStatement
                    {
                    pushFollow(FOLLOW_createIndexStatement_in_cqlStatement319);
                    st10=createIndexStatement();

                    state._fsp--;

                     stmt = st10; 

                    }
                    break;
                case 11 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:139:7: st11= dropKeyspaceStatement
                    {
                    pushFollow(FOLLOW_dropKeyspaceStatement_in_cqlStatement338);
                    st11=dropKeyspaceStatement();

                    state._fsp--;

                     stmt = st11; 

                    }
                    break;
                case 12 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:140:7: st12= dropColumnFamilyStatement
                    {
                    pushFollow(FOLLOW_dropColumnFamilyStatement_in_cqlStatement356);
                    st12=dropColumnFamilyStatement();

                    state._fsp--;

                     stmt = st12; 

                    }
                    break;
                case 13 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:141:7: st13= dropIndexStatement
                    {
                    pushFollow(FOLLOW_dropIndexStatement_in_cqlStatement370);
                    st13=dropIndexStatement();

                    state._fsp--;

                     stmt = st13; 

                    }
                    break;
                case 14 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:142:7: st14= alterTableStatement
                    {
                    pushFollow(FOLLOW_alterTableStatement_in_cqlStatement391);
                    st14=alterTableStatement();

                    state._fsp--;

                     stmt = st14; 

                    }
                    break;

            }
             if (stmt != null) stmt.setBoundTerms(currentBindMarkerIdx + 1); 
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return stmt;
    }
    // $ANTLR end "cqlStatement"


    // $ANTLR start "useStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:148:1: useStatement returns [UseStatement stmt] : K_USE ks= keyspaceName ;
    public final UseStatement useStatement() throws RecognitionException {
        UseStatement stmt = null;

        String ks = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:149:5: ( K_USE ks= keyspaceName )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:149:7: K_USE ks= keyspaceName
            {
            match(input,K_USE,FOLLOW_K_USE_in_useStatement424); 
            pushFollow(FOLLOW_keyspaceName_in_useStatement428);
            ks=keyspaceName();

            state._fsp--;

             stmt = new UseStatement(ks); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return stmt;
    }
    // $ANTLR end "useStatement"


    // $ANTLR start "selectStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:152:1: selectStatement returns [SelectStatement.RawStatement expr] : K_SELECT (sclause= selectClause | ( K_COUNT '(' sclause= selectCountClause ')' ) ) K_FROM cf= columnFamilyName ( K_USING K_CONSISTENCY K_LEVEL )? ( K_WHERE wclause= whereClause )? ( K_ORDER K_BY orderByClause[orderings] ( ',' orderByClause[orderings] )* )? ( K_LIMIT rows= INTEGER )? ;
    public final SelectStatement.RawStatement selectStatement() throws RecognitionException {
        SelectStatement.RawStatement expr = null;

        Token rows=null;
        Token K_LEVEL1=null;
        List<Selector> sclause = null;

        CFName cf = null;

        List<Relation> wclause = null;



                boolean isCount = false;
                ConsistencyLevel cLevel = ConsistencyLevel.ONE;
                int limit = 10000;
                Map<ColumnIdentifier, Boolean> orderings = new LinkedHashMap<ColumnIdentifier, Boolean>();
            
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:166:5: ( K_SELECT (sclause= selectClause | ( K_COUNT '(' sclause= selectCountClause ')' ) ) K_FROM cf= columnFamilyName ( K_USING K_CONSISTENCY K_LEVEL )? ( K_WHERE wclause= whereClause )? ( K_ORDER K_BY orderByClause[orderings] ( ',' orderByClause[orderings] )* )? ( K_LIMIT rows= INTEGER )? )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:166:7: K_SELECT (sclause= selectClause | ( K_COUNT '(' sclause= selectCountClause ')' ) ) K_FROM cf= columnFamilyName ( K_USING K_CONSISTENCY K_LEVEL )? ( K_WHERE wclause= whereClause )? ( K_ORDER K_BY orderByClause[orderings] ( ',' orderByClause[orderings] )* )? ( K_LIMIT rows= INTEGER )?
            {
            match(input,K_SELECT,FOLLOW_K_SELECT_in_selectStatement462); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:166:16: (sclause= selectClause | ( K_COUNT '(' sclause= selectCountClause ')' ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0>=K_CONSISTENCY && LA3_0<=K_LEVEL)||(LA3_0>=K_WRITETIME && LA3_0<=K_TTL)||(LA3_0>=K_VALUES && LA3_0<=K_TIMESTAMP)||(LA3_0>=K_KEY && LA3_0<=K_CLUSTERING)||LA3_0==IDENT||LA3_0==K_TYPE||LA3_0==QUOTED_NAME||(LA3_0>=K_ASCII && LA3_0<=K_TIMEUUID)||LA3_0==105) ) {
                alt3=1;
            }
            else if ( (LA3_0==K_COUNT) ) {
                int LA3_2 = input.LA(2);

                if ( (LA3_2==102) ) {
                    alt3=2;
                }
                else if ( (LA3_2==K_FROM||LA3_2==104) ) {
                    alt3=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:166:18: sclause= selectClause
                    {
                    pushFollow(FOLLOW_selectClause_in_selectStatement468);
                    sclause=selectClause();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:166:41: ( K_COUNT '(' sclause= selectCountClause ')' )
                    {
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:166:41: ( K_COUNT '(' sclause= selectCountClause ')' )
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:166:42: K_COUNT '(' sclause= selectCountClause ')'
                    {
                    match(input,K_COUNT,FOLLOW_K_COUNT_in_selectStatement473); 
                    match(input,102,FOLLOW_102_in_selectStatement475); 
                    pushFollow(FOLLOW_selectCountClause_in_selectStatement479);
                    sclause=selectCountClause();

                    state._fsp--;

                    match(input,103,FOLLOW_103_in_selectStatement481); 
                     isCount = true; 

                    }


                    }
                    break;

            }

            match(input,K_FROM,FOLLOW_K_FROM_in_selectStatement494); 
            pushFollow(FOLLOW_columnFamilyName_in_selectStatement498);
            cf=columnFamilyName();

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:168:7: ( K_USING K_CONSISTENCY K_LEVEL )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==K_USING) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:168:9: K_USING K_CONSISTENCY K_LEVEL
                    {
                    match(input,K_USING,FOLLOW_K_USING_in_selectStatement508); 
                    match(input,K_CONSISTENCY,FOLLOW_K_CONSISTENCY_in_selectStatement510); 
                    K_LEVEL1=(Token)match(input,K_LEVEL,FOLLOW_K_LEVEL_in_selectStatement512); 
                     cLevel = ConsistencyLevel.valueOf((K_LEVEL1!=null?K_LEVEL1.getText():null).toUpperCase()); 

                    }
                    break;

            }

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:169:7: ( K_WHERE wclause= whereClause )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==K_WHERE) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:169:9: K_WHERE wclause= whereClause
                    {
                    match(input,K_WHERE,FOLLOW_K_WHERE_in_selectStatement527); 
                    pushFollow(FOLLOW_whereClause_in_selectStatement531);
                    wclause=whereClause();

                    state._fsp--;


                    }
                    break;

            }

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:170:7: ( K_ORDER K_BY orderByClause[orderings] ( ',' orderByClause[orderings] )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==K_ORDER) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:170:9: K_ORDER K_BY orderByClause[orderings] ( ',' orderByClause[orderings] )*
                    {
                    match(input,K_ORDER,FOLLOW_K_ORDER_in_selectStatement544); 
                    match(input,K_BY,FOLLOW_K_BY_in_selectStatement546); 
                    pushFollow(FOLLOW_orderByClause_in_selectStatement548);
                    orderByClause(orderings);

                    state._fsp--;

                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:170:47: ( ',' orderByClause[orderings] )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==104) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:170:49: ',' orderByClause[orderings]
                    	    {
                    	    match(input,104,FOLLOW_104_in_selectStatement553); 
                    	    pushFollow(FOLLOW_orderByClause_in_selectStatement555);
                    	    orderByClause(orderings);

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    }
                    break;

            }

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:171:7: ( K_LIMIT rows= INTEGER )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==K_LIMIT) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:171:9: K_LIMIT rows= INTEGER
                    {
                    match(input,K_LIMIT,FOLLOW_K_LIMIT_in_selectStatement572); 
                    rows=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_selectStatement576); 
                     limit = Integer.parseInt((rows!=null?rows.getText():null)); 

                    }
                    break;

            }


                      SelectStatement.Parameters params = new SelectStatement.Parameters(cLevel,
                                                                                         limit,
                                                                                         orderings,
                                                                                         isCount);
                      expr = new SelectStatement.RawStatement(cf, params, sclause, wclause);
                  

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "selectStatement"


    // $ANTLR start "selectClause"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:181:1: selectClause returns [List<Selector> expr] : (t1= selector ( ',' tN= selector )* | '\\*' );
    public final List<Selector> selectClause() throws RecognitionException {
        List<Selector> expr = null;

        Selector t1 = null;

        Selector tN = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:182:5: (t1= selector ( ',' tN= selector )* | '\\*' )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==K_COUNT||(LA10_0>=K_CONSISTENCY && LA10_0<=K_LEVEL)||(LA10_0>=K_WRITETIME && LA10_0<=K_TTL)||(LA10_0>=K_VALUES && LA10_0<=K_TIMESTAMP)||(LA10_0>=K_KEY && LA10_0<=K_CLUSTERING)||LA10_0==IDENT||LA10_0==K_TYPE||LA10_0==QUOTED_NAME||(LA10_0>=K_ASCII && LA10_0<=K_TIMEUUID)) ) {
                alt10=1;
            }
            else if ( (LA10_0==105) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:182:7: t1= selector ( ',' tN= selector )*
                    {
                    pushFollow(FOLLOW_selector_in_selectClause612);
                    t1=selector();

                    state._fsp--;

                     expr = new ArrayList<Selector>(); expr.add(t1); 
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:182:73: ( ',' tN= selector )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==104) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:182:74: ',' tN= selector
                    	    {
                    	    match(input,104,FOLLOW_104_in_selectClause617); 
                    	    pushFollow(FOLLOW_selector_in_selectClause621);
                    	    tN=selector();

                    	    state._fsp--;

                    	     expr.add(tN); 

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:183:7: '\\*'
                    {
                    match(input,105,FOLLOW_105_in_selectClause633); 
                     expr = Collections.<Selector>emptyList();

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "selectClause"


    // $ANTLR start "selector"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:186:1: selector returns [Selector s] : (c= cident | K_WRITETIME '(' c= cident ')' | K_TTL '(' c= cident ')' );
    public final Selector selector() throws RecognitionException {
        Selector s = null;

        ColumnIdentifier c = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:187:5: (c= cident | K_WRITETIME '(' c= cident ')' | K_TTL '(' c= cident ')' )
            int alt11=3;
            switch ( input.LA(1) ) {
            case K_COUNT:
            case K_CONSISTENCY:
            case K_LEVEL:
            case K_VALUES:
            case K_TIMESTAMP:
            case K_KEY:
            case K_COMPACT:
            case K_STORAGE:
            case K_CLUSTERING:
            case IDENT:
            case K_TYPE:
            case QUOTED_NAME:
            case K_ASCII:
            case K_BIGINT:
            case K_BLOB:
            case K_BOOLEAN:
            case K_COUNTER:
            case K_DECIMAL:
            case K_DOUBLE:
            case K_FLOAT:
            case K_INT:
            case K_TEXT:
            case K_UUID:
            case K_VARCHAR:
            case K_VARINT:
            case K_TIMEUUID:
                {
                alt11=1;
                }
                break;
            case K_WRITETIME:
                {
                int LA11_2 = input.LA(2);

                if ( (LA11_2==102) ) {
                    alt11=2;
                }
                else if ( (LA11_2==K_FROM||LA11_2==104) ) {
                    alt11=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 2, input);

                    throw nvae;
                }
                }
                break;
            case K_TTL:
                {
                int LA11_3 = input.LA(2);

                if ( (LA11_3==102) ) {
                    alt11=3;
                }
                else if ( (LA11_3==K_FROM||LA11_3==104) ) {
                    alt11=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:187:7: c= cident
                    {
                    pushFollow(FOLLOW_cident_in_selector658);
                    c=cident();

                    state._fsp--;

                     s = c; 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:188:7: K_WRITETIME '(' c= cident ')'
                    {
                    match(input,K_WRITETIME,FOLLOW_K_WRITETIME_in_selector680); 
                    match(input,102,FOLLOW_102_in_selector682); 
                    pushFollow(FOLLOW_cident_in_selector686);
                    c=cident();

                    state._fsp--;

                    match(input,103,FOLLOW_103_in_selector688); 
                     s = new Selector.WithFunction(c, Selector.Function.WRITE_TIME); 

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:189:7: K_TTL '(' c= cident ')'
                    {
                    match(input,K_TTL,FOLLOW_K_TTL_in_selector698); 
                    match(input,102,FOLLOW_102_in_selector700); 
                    pushFollow(FOLLOW_cident_in_selector704);
                    c=cident();

                    state._fsp--;

                    match(input,103,FOLLOW_103_in_selector706); 
                     s = new Selector.WithFunction(c, Selector.Function.TTL); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return s;
    }
    // $ANTLR end "selector"


    // $ANTLR start "selectCountClause"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:192:1: selectCountClause returns [List<Selector> expr] : (ids= cidentList | '\\*' | i= INTEGER );
    public final List<Selector> selectCountClause() throws RecognitionException {
        List<Selector> expr = null;

        Token i=null;
        List<ColumnIdentifier> ids = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:193:5: (ids= cidentList | '\\*' | i= INTEGER )
            int alt12=3;
            switch ( input.LA(1) ) {
            case K_COUNT:
            case K_CONSISTENCY:
            case K_LEVEL:
            case K_WRITETIME:
            case K_TTL:
            case K_VALUES:
            case K_TIMESTAMP:
            case K_KEY:
            case K_COMPACT:
            case K_STORAGE:
            case K_CLUSTERING:
            case IDENT:
            case K_TYPE:
            case QUOTED_NAME:
            case K_ASCII:
            case K_BIGINT:
            case K_BLOB:
            case K_BOOLEAN:
            case K_COUNTER:
            case K_DECIMAL:
            case K_DOUBLE:
            case K_FLOAT:
            case K_INT:
            case K_TEXT:
            case K_UUID:
            case K_VARCHAR:
            case K_VARINT:
            case K_TIMEUUID:
                {
                alt12=1;
                }
                break;
            case 105:
                {
                alt12=2;
                }
                break;
            case INTEGER:
                {
                alt12=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }

            switch (alt12) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:193:7: ids= cidentList
                    {
                    pushFollow(FOLLOW_cidentList_in_selectCountClause737);
                    ids=cidentList();

                    state._fsp--;

                     expr = new ArrayList<Selector>(ids); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:194:7: '\\*'
                    {
                    match(input,105,FOLLOW_105_in_selectCountClause747); 
                     expr = Collections.<Selector>emptyList();

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:195:7: i= INTEGER
                    {
                    i=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_selectCountClause769); 
                     if (!i.getText().equals("1")) addRecognitionError("Only COUNT(1) is supported, got COUNT(" + i.getText() + ")"); expr = Collections.<Selector>emptyList();

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "selectCountClause"


    // $ANTLR start "whereClause"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:198:1: whereClause returns [List<Relation> clause] : first= relation ( K_AND next= relation )* ;
    public final List<Relation> whereClause() throws RecognitionException {
        List<Relation> clause = null;

        Relation first = null;

        Relation next = null;


         clause = new ArrayList<Relation>(); 
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:200:5: (first= relation ( K_AND next= relation )* )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:200:7: first= relation ( K_AND next= relation )*
            {
            pushFollow(FOLLOW_relation_in_whereClause807);
            first=relation();

            state._fsp--;

             clause.add(first); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:200:46: ( K_AND next= relation )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==K_AND) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:200:47: K_AND next= relation
            	    {
            	    match(input,K_AND,FOLLOW_K_AND_in_whereClause812); 
            	    pushFollow(FOLLOW_relation_in_whereClause816);
            	    next=relation();

            	    state._fsp--;

            	     clause.add(next); 

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return clause;
    }
    // $ANTLR end "whereClause"


    // $ANTLR start "orderByClause"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:203:1: orderByClause[Map<ColumnIdentifier, Boolean> orderings] : c= cident ( K_ASC | K_DESC )? ;
    public final void orderByClause(Map<ColumnIdentifier, Boolean> orderings) throws RecognitionException {
        ColumnIdentifier c = null;



                ColumnIdentifier orderBy = null;
                boolean reversed = false;
            
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:208:5: (c= cident ( K_ASC | K_DESC )? )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:208:7: c= cident ( K_ASC | K_DESC )?
            {
            pushFollow(FOLLOW_cident_in_orderByClause848);
            c=cident();

            state._fsp--;

             orderBy = c; 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:208:33: ( K_ASC | K_DESC )?
            int alt14=3;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==K_ASC) ) {
                alt14=1;
            }
            else if ( (LA14_0==K_DESC) ) {
                alt14=2;
            }
            switch (alt14) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:208:34: K_ASC
                    {
                    match(input,K_ASC,FOLLOW_K_ASC_in_orderByClause853); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:208:42: K_DESC
                    {
                    match(input,K_DESC,FOLLOW_K_DESC_in_orderByClause857); 
                     reversed = true; 

                    }
                    break;

            }

             orderings.put(c, reversed); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "orderByClause"


    // $ANTLR start "insertStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:211:1: insertStatement returns [UpdateStatement expr] : K_INSERT K_INTO cf= columnFamilyName '(' c1= cident ( ',' cn= cident )+ ')' K_VALUES '(' v1= term ( ',' vn= term )+ ')' ( usingClause[attrs] )? ;
    public final UpdateStatement insertStatement() throws RecognitionException {
        UpdateStatement expr = null;

        CFName cf = null;

        ColumnIdentifier c1 = null;

        ColumnIdentifier cn = null;

        Term v1 = null;

        Term vn = null;



                Attributes attrs = new Attributes();
                List<ColumnIdentifier> columnNames  = new ArrayList<ColumnIdentifier>();
                List<Term> columnValues = new ArrayList<Term>();
            
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:224:5: ( K_INSERT K_INTO cf= columnFamilyName '(' c1= cident ( ',' cn= cident )+ ')' K_VALUES '(' v1= term ( ',' vn= term )+ ')' ( usingClause[attrs] )? )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:224:7: K_INSERT K_INTO cf= columnFamilyName '(' c1= cident ( ',' cn= cident )+ ')' K_VALUES '(' v1= term ( ',' vn= term )+ ')' ( usingClause[attrs] )?
            {
            match(input,K_INSERT,FOLLOW_K_INSERT_in_insertStatement895); 
            match(input,K_INTO,FOLLOW_K_INTO_in_insertStatement897); 
            pushFollow(FOLLOW_columnFamilyName_in_insertStatement901);
            cf=columnFamilyName();

            state._fsp--;

            match(input,102,FOLLOW_102_in_insertStatement913); 
            pushFollow(FOLLOW_cident_in_insertStatement917);
            c1=cident();

            state._fsp--;

             columnNames.add(c1); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:225:51: ( ',' cn= cident )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==104) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:225:53: ',' cn= cident
            	    {
            	    match(input,104,FOLLOW_104_in_insertStatement924); 
            	    pushFollow(FOLLOW_cident_in_insertStatement928);
            	    cn=cident();

            	    state._fsp--;

            	     columnNames.add(cn); 

            	    }
            	    break;

            	default :
            	    if ( cnt15 >= 1 ) break loop15;
                        EarlyExitException eee =
                            new EarlyExitException(15, input);
                        throw eee;
                }
                cnt15++;
            } while (true);

            match(input,103,FOLLOW_103_in_insertStatement935); 
            match(input,K_VALUES,FOLLOW_K_VALUES_in_insertStatement945); 
            match(input,102,FOLLOW_102_in_insertStatement957); 
            pushFollow(FOLLOW_term_in_insertStatement961);
            v1=term();

            state._fsp--;

             columnValues.add(v1); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:227:49: ( ',' vn= term )+
            int cnt16=0;
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==104) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:227:51: ',' vn= term
            	    {
            	    match(input,104,FOLLOW_104_in_insertStatement967); 
            	    pushFollow(FOLLOW_term_in_insertStatement971);
            	    vn=term();

            	    state._fsp--;

            	     columnValues.add(vn); 

            	    }
            	    break;

            	default :
            	    if ( cnt16 >= 1 ) break loop16;
                        EarlyExitException eee =
                            new EarlyExitException(16, input);
                        throw eee;
                }
                cnt16++;
            } while (true);

            match(input,103,FOLLOW_103_in_insertStatement978); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:228:9: ( usingClause[attrs] )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==K_USING) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:228:11: usingClause[attrs]
                    {
                    pushFollow(FOLLOW_usingClause_in_insertStatement990);
                    usingClause(attrs);

                    state._fsp--;


                    }
                    break;

            }


                      expr = new UpdateStatement(cf, columnNames, columnValues, attrs);
                  

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "insertStatement"


    // $ANTLR start "usingClause"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:234:1: usingClause[Attributes attrs] : K_USING usingClauseObjective[attrs] ( ( K_AND )? usingClauseObjective[attrs] )* ;
    public final void usingClause(Attributes attrs) throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:235:5: ( K_USING usingClauseObjective[attrs] ( ( K_AND )? usingClauseObjective[attrs] )* )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:235:7: K_USING usingClauseObjective[attrs] ( ( K_AND )? usingClauseObjective[attrs] )*
            {
            match(input,K_USING,FOLLOW_K_USING_in_usingClause1020); 
            pushFollow(FOLLOW_usingClauseObjective_in_usingClause1022);
            usingClauseObjective(attrs);

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:235:43: ( ( K_AND )? usingClauseObjective[attrs] )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==K_CONSISTENCY||(LA19_0>=K_TTL && LA19_0<=K_AND)||LA19_0==K_TIMESTAMP) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:235:45: ( K_AND )? usingClauseObjective[attrs]
            	    {
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:235:45: ( K_AND )?
            	    int alt18=2;
            	    int LA18_0 = input.LA(1);

            	    if ( (LA18_0==K_AND) ) {
            	        alt18=1;
            	    }
            	    switch (alt18) {
            	        case 1 :
            	            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:235:45: K_AND
            	            {
            	            match(input,K_AND,FOLLOW_K_AND_in_usingClause1027); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_usingClauseObjective_in_usingClause1030);
            	    usingClauseObjective(attrs);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "usingClause"


    // $ANTLR start "usingClauseDelete"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:238:1: usingClauseDelete[Attributes attrs] : K_USING usingClauseDeleteObjective[attrs] ( ( K_AND )? usingClauseDeleteObjective[attrs] )* ;
    public final void usingClauseDelete(Attributes attrs) throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:239:5: ( K_USING usingClauseDeleteObjective[attrs] ( ( K_AND )? usingClauseDeleteObjective[attrs] )* )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:239:7: K_USING usingClauseDeleteObjective[attrs] ( ( K_AND )? usingClauseDeleteObjective[attrs] )*
            {
            match(input,K_USING,FOLLOW_K_USING_in_usingClauseDelete1052); 
            pushFollow(FOLLOW_usingClauseDeleteObjective_in_usingClauseDelete1054);
            usingClauseDeleteObjective(attrs);

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:239:49: ( ( K_AND )? usingClauseDeleteObjective[attrs] )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==K_CONSISTENCY||LA21_0==K_AND||LA21_0==K_TIMESTAMP) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:239:51: ( K_AND )? usingClauseDeleteObjective[attrs]
            	    {
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:239:51: ( K_AND )?
            	    int alt20=2;
            	    int LA20_0 = input.LA(1);

            	    if ( (LA20_0==K_AND) ) {
            	        alt20=1;
            	    }
            	    switch (alt20) {
            	        case 1 :
            	            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:239:51: K_AND
            	            {
            	            match(input,K_AND,FOLLOW_K_AND_in_usingClauseDelete1059); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_usingClauseDeleteObjective_in_usingClauseDelete1062);
            	    usingClauseDeleteObjective(attrs);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "usingClauseDelete"


    // $ANTLR start "usingClauseDeleteObjective"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:242:1: usingClauseDeleteObjective[Attributes attrs] : ( K_CONSISTENCY K_LEVEL | K_TIMESTAMP ts= INTEGER );
    public final void usingClauseDeleteObjective(Attributes attrs) throws RecognitionException {
        Token ts=null;
        Token K_LEVEL2=null;

        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:243:5: ( K_CONSISTENCY K_LEVEL | K_TIMESTAMP ts= INTEGER )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==K_CONSISTENCY) ) {
                alt22=1;
            }
            else if ( (LA22_0==K_TIMESTAMP) ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:243:7: K_CONSISTENCY K_LEVEL
                    {
                    match(input,K_CONSISTENCY,FOLLOW_K_CONSISTENCY_in_usingClauseDeleteObjective1084); 
                    K_LEVEL2=(Token)match(input,K_LEVEL,FOLLOW_K_LEVEL_in_usingClauseDeleteObjective1086); 
                     attrs.cLevel = ConsistencyLevel.valueOf((K_LEVEL2!=null?K_LEVEL2.getText():null).toUpperCase()); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:244:7: K_TIMESTAMP ts= INTEGER
                    {
                    match(input,K_TIMESTAMP,FOLLOW_K_TIMESTAMP_in_usingClauseDeleteObjective1097); 
                    ts=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_usingClauseDeleteObjective1101); 
                     attrs.timestamp = Long.valueOf((ts!=null?ts.getText():null)); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "usingClauseDeleteObjective"


    // $ANTLR start "usingClauseObjective"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:247:1: usingClauseObjective[Attributes attrs] : ( usingClauseDeleteObjective[attrs] | K_TTL t= INTEGER );
    public final void usingClauseObjective(Attributes attrs) throws RecognitionException {
        Token t=null;

        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:248:5: ( usingClauseDeleteObjective[attrs] | K_TTL t= INTEGER )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==K_CONSISTENCY||LA23_0==K_TIMESTAMP) ) {
                alt23=1;
            }
            else if ( (LA23_0==K_TTL) ) {
                alt23=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }
            switch (alt23) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:248:7: usingClauseDeleteObjective[attrs]
                    {
                    pushFollow(FOLLOW_usingClauseDeleteObjective_in_usingClauseObjective1121);
                    usingClauseDeleteObjective(attrs);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:249:7: K_TTL t= INTEGER
                    {
                    match(input,K_TTL,FOLLOW_K_TTL_in_usingClauseObjective1130); 
                    t=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_usingClauseObjective1134); 
                     attrs.timeToLive = Integer.valueOf((t!=null?t.getText():null)); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "usingClauseObjective"


    // $ANTLR start "updateStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:252:1: updateStatement returns [UpdateStatement expr] : K_UPDATE cf= columnFamilyName ( usingClause[attrs] )? K_SET termPairWithOperation[columns] ( ',' termPairWithOperation[columns] )* K_WHERE wclause= whereClause ;
    public final UpdateStatement updateStatement() throws RecognitionException {
        UpdateStatement expr = null;

        CFName cf = null;

        List<Relation> wclause = null;



                Attributes attrs = new Attributes();
                Map<ColumnIdentifier, Operation> columns = new HashMap<ColumnIdentifier, Operation>();
            
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:263:5: ( K_UPDATE cf= columnFamilyName ( usingClause[attrs] )? K_SET termPairWithOperation[columns] ( ',' termPairWithOperation[columns] )* K_WHERE wclause= whereClause )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:263:7: K_UPDATE cf= columnFamilyName ( usingClause[attrs] )? K_SET termPairWithOperation[columns] ( ',' termPairWithOperation[columns] )* K_WHERE wclause= whereClause
            {
            match(input,K_UPDATE,FOLLOW_K_UPDATE_in_updateStatement1168); 
            pushFollow(FOLLOW_columnFamilyName_in_updateStatement1172);
            cf=columnFamilyName();

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:264:7: ( usingClause[attrs] )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==K_USING) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:264:9: usingClause[attrs]
                    {
                    pushFollow(FOLLOW_usingClause_in_updateStatement1182);
                    usingClause(attrs);

                    state._fsp--;


                    }
                    break;

            }

            match(input,K_SET,FOLLOW_K_SET_in_updateStatement1194); 
            pushFollow(FOLLOW_termPairWithOperation_in_updateStatement1196);
            termPairWithOperation(columns);

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:265:44: ( ',' termPairWithOperation[columns] )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==104) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:265:45: ',' termPairWithOperation[columns]
            	    {
            	    match(input,104,FOLLOW_104_in_updateStatement1200); 
            	    pushFollow(FOLLOW_termPairWithOperation_in_updateStatement1202);
            	    termPairWithOperation(columns);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop25;
                }
            } while (true);

            match(input,K_WHERE,FOLLOW_K_WHERE_in_updateStatement1213); 
            pushFollow(FOLLOW_whereClause_in_updateStatement1217);
            wclause=whereClause();

            state._fsp--;


                      return new UpdateStatement(cf, columns, wclause, attrs);
                  

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "updateStatement"


    // $ANTLR start "deleteStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:272:1: deleteStatement returns [DeleteStatement expr] : K_DELETE (ids= cidentList )? K_FROM cf= columnFamilyName ( usingClauseDelete[attrs] )? K_WHERE wclause= whereClause ;
    public final DeleteStatement deleteStatement() throws RecognitionException {
        DeleteStatement expr = null;

        List<ColumnIdentifier> ids = null;

        CFName cf = null;

        List<Relation> wclause = null;



                Attributes attrs = new Attributes();
                List<ColumnIdentifier> columnsList = Collections.emptyList();
            
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:283:5: ( K_DELETE (ids= cidentList )? K_FROM cf= columnFamilyName ( usingClauseDelete[attrs] )? K_WHERE wclause= whereClause )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:283:7: K_DELETE (ids= cidentList )? K_FROM cf= columnFamilyName ( usingClauseDelete[attrs] )? K_WHERE wclause= whereClause
            {
            match(input,K_DELETE,FOLLOW_K_DELETE_in_deleteStatement1257); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:283:16: (ids= cidentList )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==K_COUNT||(LA26_0>=K_CONSISTENCY && LA26_0<=K_LEVEL)||(LA26_0>=K_WRITETIME && LA26_0<=K_TTL)||(LA26_0>=K_VALUES && LA26_0<=K_TIMESTAMP)||(LA26_0>=K_KEY && LA26_0<=K_CLUSTERING)||LA26_0==IDENT||LA26_0==K_TYPE||LA26_0==QUOTED_NAME||(LA26_0>=K_ASCII && LA26_0<=K_TIMEUUID)) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:283:18: ids= cidentList
                    {
                    pushFollow(FOLLOW_cidentList_in_deleteStatement1263);
                    ids=cidentList();

                    state._fsp--;

                     columnsList = ids; 

                    }
                    break;

            }

            match(input,K_FROM,FOLLOW_K_FROM_in_deleteStatement1276); 
            pushFollow(FOLLOW_columnFamilyName_in_deleteStatement1280);
            cf=columnFamilyName();

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:285:7: ( usingClauseDelete[attrs] )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==K_USING) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:285:9: usingClauseDelete[attrs]
                    {
                    pushFollow(FOLLOW_usingClauseDelete_in_deleteStatement1290);
                    usingClauseDelete(attrs);

                    state._fsp--;


                    }
                    break;

            }

            match(input,K_WHERE,FOLLOW_K_WHERE_in_deleteStatement1302); 
            pushFollow(FOLLOW_whereClause_in_deleteStatement1306);
            wclause=whereClause();

            state._fsp--;


                      return new DeleteStatement(cf, columnsList, wclause, attrs);
                  

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "deleteStatement"


    // $ANTLR start "batchStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:293:1: batchStatement returns [BatchStatement expr] : K_BEGIN K_BATCH ( usingClause[attrs] )? s1= batchStatementObjective ( ';' )? (sN= batchStatementObjective ( ';' )? )* K_APPLY K_BATCH ;
    public final BatchStatement batchStatement() throws RecognitionException {
        BatchStatement expr = null;

        ModificationStatement s1 = null;

        ModificationStatement sN = null;



                Attributes attrs = new Attributes();
                List<ModificationStatement> statements = new ArrayList<ModificationStatement>();
            
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:322:5: ( K_BEGIN K_BATCH ( usingClause[attrs] )? s1= batchStatementObjective ( ';' )? (sN= batchStatementObjective ( ';' )? )* K_APPLY K_BATCH )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:322:7: K_BEGIN K_BATCH ( usingClause[attrs] )? s1= batchStatementObjective ( ';' )? (sN= batchStatementObjective ( ';' )? )* K_APPLY K_BATCH
            {
            match(input,K_BEGIN,FOLLOW_K_BEGIN_in_batchStatement1347); 
            match(input,K_BATCH,FOLLOW_K_BATCH_in_batchStatement1349); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:322:23: ( usingClause[attrs] )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==K_USING) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:322:25: usingClause[attrs]
                    {
                    pushFollow(FOLLOW_usingClause_in_batchStatement1353);
                    usingClause(attrs);

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_batchStatementObjective_in_batchStatement1371);
            s1=batchStatementObjective();

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:323:38: ( ';' )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==101) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:323:38: ';'
                    {
                    match(input,101,FOLLOW_101_in_batchStatement1373); 

                    }
                    break;

            }

             statements.add(s1); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:323:67: (sN= batchStatementObjective ( ';' )? )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==K_INSERT||LA31_0==K_UPDATE||LA31_0==K_DELETE) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:323:69: sN= batchStatementObjective ( ';' )?
            	    {
            	    pushFollow(FOLLOW_batchStatementObjective_in_batchStatement1382);
            	    sN=batchStatementObjective();

            	    state._fsp--;

            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:323:96: ( ';' )?
            	    int alt30=2;
            	    int LA30_0 = input.LA(1);

            	    if ( (LA30_0==101) ) {
            	        alt30=1;
            	    }
            	    switch (alt30) {
            	        case 1 :
            	            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:323:96: ';'
            	            {
            	            match(input,101,FOLLOW_101_in_batchStatement1384); 

            	            }
            	            break;

            	    }

            	     statements.add(sN); 

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);

            match(input,K_APPLY,FOLLOW_K_APPLY_in_batchStatement1398); 
            match(input,K_BATCH,FOLLOW_K_BATCH_in_batchStatement1400); 

                      return new BatchStatement(statements, attrs);
                  

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "batchStatement"


    // $ANTLR start "batchStatementObjective"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:330:1: batchStatementObjective returns [ModificationStatement statement] : (i= insertStatement | u= updateStatement | d= deleteStatement );
    public final ModificationStatement batchStatementObjective() throws RecognitionException {
        ModificationStatement statement = null;

        UpdateStatement i = null;

        UpdateStatement u = null;

        DeleteStatement d = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:331:5: (i= insertStatement | u= updateStatement | d= deleteStatement )
            int alt32=3;
            switch ( input.LA(1) ) {
            case K_INSERT:
                {
                alt32=1;
                }
                break;
            case K_UPDATE:
                {
                alt32=2;
                }
                break;
            case K_DELETE:
                {
                alt32=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }

            switch (alt32) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:331:7: i= insertStatement
                    {
                    pushFollow(FOLLOW_insertStatement_in_batchStatementObjective1431);
                    i=insertStatement();

                    state._fsp--;

                     statement = i; 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:332:7: u= updateStatement
                    {
                    pushFollow(FOLLOW_updateStatement_in_batchStatementObjective1444);
                    u=updateStatement();

                    state._fsp--;

                     statement = u; 

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:333:7: d= deleteStatement
                    {
                    pushFollow(FOLLOW_deleteStatement_in_batchStatementObjective1457);
                    d=deleteStatement();

                    state._fsp--;

                     statement = d; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return statement;
    }
    // $ANTLR end "batchStatementObjective"


    // $ANTLR start "createKeyspaceStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:336:1: createKeyspaceStatement returns [CreateKeyspaceStatement expr] : K_CREATE K_KEYSPACE ks= keyspaceName K_WITH props= properties ;
    public final CreateKeyspaceStatement createKeyspaceStatement() throws RecognitionException {
        CreateKeyspaceStatement expr = null;

        String ks = null;

        Map<String, String> props = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:340:5: ( K_CREATE K_KEYSPACE ks= keyspaceName K_WITH props= properties )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:340:7: K_CREATE K_KEYSPACE ks= keyspaceName K_WITH props= properties
            {
            match(input,K_CREATE,FOLLOW_K_CREATE_in_createKeyspaceStatement1483); 
            match(input,K_KEYSPACE,FOLLOW_K_KEYSPACE_in_createKeyspaceStatement1485); 
            pushFollow(FOLLOW_keyspaceName_in_createKeyspaceStatement1489);
            ks=keyspaceName();

            state._fsp--;

            match(input,K_WITH,FOLLOW_K_WITH_in_createKeyspaceStatement1497); 
            pushFollow(FOLLOW_properties_in_createKeyspaceStatement1501);
            props=properties();

            state._fsp--;

             expr = new CreateKeyspaceStatement(ks, props); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "createKeyspaceStatement"


    // $ANTLR start "createColumnFamilyStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:344:1: createColumnFamilyStatement returns [CreateColumnFamilyStatement.RawStatement expr] : K_CREATE K_COLUMNFAMILY cf= columnFamilyName cfamDefinition[expr] ;
    public final CreateColumnFamilyStatement.RawStatement createColumnFamilyStatement() throws RecognitionException {
        CreateColumnFamilyStatement.RawStatement expr = null;

        CFName cf = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:352:5: ( K_CREATE K_COLUMNFAMILY cf= columnFamilyName cfamDefinition[expr] )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:352:7: K_CREATE K_COLUMNFAMILY cf= columnFamilyName cfamDefinition[expr]
            {
            match(input,K_CREATE,FOLLOW_K_CREATE_in_createColumnFamilyStatement1526); 
            match(input,K_COLUMNFAMILY,FOLLOW_K_COLUMNFAMILY_in_createColumnFamilyStatement1528); 
            pushFollow(FOLLOW_columnFamilyName_in_createColumnFamilyStatement1532);
            cf=columnFamilyName();

            state._fsp--;

             expr = new CreateColumnFamilyStatement.RawStatement(cf); 
            pushFollow(FOLLOW_cfamDefinition_in_createColumnFamilyStatement1542);
            cfamDefinition(expr);

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "createColumnFamilyStatement"


    // $ANTLR start "cfamDefinition"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:356:1: cfamDefinition[CreateColumnFamilyStatement.RawStatement expr] : '(' cfamColumns[expr] ( ',' ( cfamColumns[expr] )? )* ')' ( K_WITH cfamProperty[expr] ( K_AND cfamProperty[expr] )* )? ;
    public final void cfamDefinition(CreateColumnFamilyStatement.RawStatement expr) throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:357:5: ( '(' cfamColumns[expr] ( ',' ( cfamColumns[expr] )? )* ')' ( K_WITH cfamProperty[expr] ( K_AND cfamProperty[expr] )* )? )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:357:7: '(' cfamColumns[expr] ( ',' ( cfamColumns[expr] )? )* ')' ( K_WITH cfamProperty[expr] ( K_AND cfamProperty[expr] )* )?
            {
            match(input,102,FOLLOW_102_in_cfamDefinition1561); 
            pushFollow(FOLLOW_cfamColumns_in_cfamDefinition1563);
            cfamColumns(expr);

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:357:29: ( ',' ( cfamColumns[expr] )? )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==104) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:357:31: ',' ( cfamColumns[expr] )?
            	    {
            	    match(input,104,FOLLOW_104_in_cfamDefinition1568); 
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:357:35: ( cfamColumns[expr] )?
            	    int alt33=2;
            	    int LA33_0 = input.LA(1);

            	    if ( (LA33_0==K_COUNT||(LA33_0>=K_CONSISTENCY && LA33_0<=K_LEVEL)||(LA33_0>=K_WRITETIME && LA33_0<=K_TTL)||(LA33_0>=K_VALUES && LA33_0<=K_TIMESTAMP)||(LA33_0>=K_PRIMARY && LA33_0<=K_CLUSTERING)||LA33_0==IDENT||LA33_0==K_TYPE||LA33_0==QUOTED_NAME||(LA33_0>=K_ASCII && LA33_0<=K_TIMEUUID)) ) {
            	        alt33=1;
            	    }
            	    switch (alt33) {
            	        case 1 :
            	            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:357:35: cfamColumns[expr]
            	            {
            	            pushFollow(FOLLOW_cfamColumns_in_cfamDefinition1570);
            	            cfamColumns(expr);

            	            state._fsp--;


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);

            match(input,103,FOLLOW_103_in_cfamDefinition1577); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:358:7: ( K_WITH cfamProperty[expr] ( K_AND cfamProperty[expr] )* )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==K_WITH) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:358:9: K_WITH cfamProperty[expr] ( K_AND cfamProperty[expr] )*
                    {
                    match(input,K_WITH,FOLLOW_K_WITH_in_cfamDefinition1587); 
                    pushFollow(FOLLOW_cfamProperty_in_cfamDefinition1589);
                    cfamProperty(expr);

                    state._fsp--;

                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:358:35: ( K_AND cfamProperty[expr] )*
                    loop35:
                    do {
                        int alt35=2;
                        int LA35_0 = input.LA(1);

                        if ( (LA35_0==K_AND) ) {
                            alt35=1;
                        }


                        switch (alt35) {
                    	case 1 :
                    	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:358:37: K_AND cfamProperty[expr]
                    	    {
                    	    match(input,K_AND,FOLLOW_K_AND_in_cfamDefinition1594); 
                    	    pushFollow(FOLLOW_cfamProperty_in_cfamDefinition1596);
                    	    cfamProperty(expr);

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop35;
                        }
                    } while (true);


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "cfamDefinition"


    // $ANTLR start "cfamColumns"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:361:1: cfamColumns[CreateColumnFamilyStatement.RawStatement expr] : (k= cident v= comparatorType ( K_PRIMARY K_KEY )? | K_PRIMARY K_KEY '(' k= cident ( ',' c= cident )* ')' );
    public final void cfamColumns(CreateColumnFamilyStatement.RawStatement expr) throws RecognitionException {
        ColumnIdentifier k = null;

        String v = null;

        ColumnIdentifier c = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:362:5: (k= cident v= comparatorType ( K_PRIMARY K_KEY )? | K_PRIMARY K_KEY '(' k= cident ( ',' c= cident )* ')' )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==K_COUNT||(LA39_0>=K_CONSISTENCY && LA39_0<=K_LEVEL)||(LA39_0>=K_WRITETIME && LA39_0<=K_TTL)||(LA39_0>=K_VALUES && LA39_0<=K_TIMESTAMP)||(LA39_0>=K_KEY && LA39_0<=K_CLUSTERING)||LA39_0==IDENT||LA39_0==K_TYPE||LA39_0==QUOTED_NAME||(LA39_0>=K_ASCII && LA39_0<=K_TIMEUUID)) ) {
                alt39=1;
            }
            else if ( (LA39_0==K_PRIMARY) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:362:7: k= cident v= comparatorType ( K_PRIMARY K_KEY )?
                    {
                    pushFollow(FOLLOW_cident_in_cfamColumns1622);
                    k=cident();

                    state._fsp--;

                    pushFollow(FOLLOW_comparatorType_in_cfamColumns1626);
                    v=comparatorType();

                    state._fsp--;

                     expr.addDefinition(k, v); 
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:362:64: ( K_PRIMARY K_KEY )?
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==K_PRIMARY) ) {
                        alt37=1;
                    }
                    switch (alt37) {
                        case 1 :
                            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:362:65: K_PRIMARY K_KEY
                            {
                            match(input,K_PRIMARY,FOLLOW_K_PRIMARY_in_cfamColumns1631); 
                            match(input,K_KEY,FOLLOW_K_KEY_in_cfamColumns1633); 
                             expr.setKeyAlias(k); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:363:7: K_PRIMARY K_KEY '(' k= cident ( ',' c= cident )* ')'
                    {
                    match(input,K_PRIMARY,FOLLOW_K_PRIMARY_in_cfamColumns1645); 
                    match(input,K_KEY,FOLLOW_K_KEY_in_cfamColumns1647); 
                    match(input,102,FOLLOW_102_in_cfamColumns1649); 
                    pushFollow(FOLLOW_cident_in_cfamColumns1653);
                    k=cident();

                    state._fsp--;

                     expr.setKeyAlias(k); 
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:363:62: ( ',' c= cident )*
                    loop38:
                    do {
                        int alt38=2;
                        int LA38_0 = input.LA(1);

                        if ( (LA38_0==104) ) {
                            alt38=1;
                        }


                        switch (alt38) {
                    	case 1 :
                    	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:363:63: ',' c= cident
                    	    {
                    	    match(input,104,FOLLOW_104_in_cfamColumns1658); 
                    	    pushFollow(FOLLOW_cident_in_cfamColumns1662);
                    	    c=cident();

                    	    state._fsp--;

                    	     expr.addColumnAlias(c); 

                    	    }
                    	    break;

                    	default :
                    	    break loop38;
                        }
                    } while (true);

                    match(input,103,FOLLOW_103_in_cfamColumns1669); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "cfamColumns"


    // $ANTLR start "cfamProperty"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:366:1: cfamProperty[CreateColumnFamilyStatement.RawStatement expr] : (k= property '=' v= propertyValue | K_COMPACT K_STORAGE | K_CLUSTERING K_ORDER K_BY '(' cfamOrdering[expr] ( ',' cfamOrdering[expr] )* ')' );
    public final void cfamProperty(CreateColumnFamilyStatement.RawStatement expr) throws RecognitionException {
        String k = null;

        String v = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:367:5: (k= property '=' v= propertyValue | K_COMPACT K_STORAGE | K_CLUSTERING K_ORDER K_BY '(' cfamOrdering[expr] ( ',' cfamOrdering[expr] )* ')' )
            int alt41=3;
            switch ( input.LA(1) ) {
            case K_COUNT:
            case K_CONSISTENCY:
            case K_LEVEL:
            case K_WRITETIME:
            case K_TTL:
            case K_VALUES:
            case K_TIMESTAMP:
            case K_KEY:
            case K_STORAGE:
            case IDENT:
            case K_TYPE:
            case QUOTED_NAME:
            case K_ASCII:
            case K_BIGINT:
            case K_BLOB:
            case K_BOOLEAN:
            case K_COUNTER:
            case K_DECIMAL:
            case K_DOUBLE:
            case K_FLOAT:
            case K_INT:
            case K_TEXT:
            case K_UUID:
            case K_VARCHAR:
            case K_VARINT:
            case K_TIMEUUID:
                {
                alt41=1;
                }
                break;
            case K_COMPACT:
                {
                int LA41_2 = input.LA(2);

                if ( (LA41_2==K_STORAGE) ) {
                    alt41=2;
                }
                else if ( (LA41_2==106||LA41_2==110) ) {
                    alt41=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 41, 2, input);

                    throw nvae;
                }
                }
                break;
            case K_CLUSTERING:
                {
                int LA41_3 = input.LA(2);

                if ( (LA41_3==K_ORDER) ) {
                    alt41=3;
                }
                else if ( (LA41_3==106||LA41_3==110) ) {
                    alt41=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 41, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;
            }

            switch (alt41) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:367:7: k= property '=' v= propertyValue
                    {
                    pushFollow(FOLLOW_property_in_cfamProperty1689);
                    k=property();

                    state._fsp--;

                    match(input,106,FOLLOW_106_in_cfamProperty1691); 
                    pushFollow(FOLLOW_propertyValue_in_cfamProperty1695);
                    v=propertyValue();

                    state._fsp--;

                     expr.addProperty(k, v); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:368:7: K_COMPACT K_STORAGE
                    {
                    match(input,K_COMPACT,FOLLOW_K_COMPACT_in_cfamProperty1705); 
                    match(input,K_STORAGE,FOLLOW_K_STORAGE_in_cfamProperty1707); 
                     expr.setCompactStorage(); 

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:369:7: K_CLUSTERING K_ORDER K_BY '(' cfamOrdering[expr] ( ',' cfamOrdering[expr] )* ')'
                    {
                    match(input,K_CLUSTERING,FOLLOW_K_CLUSTERING_in_cfamProperty1717); 
                    match(input,K_ORDER,FOLLOW_K_ORDER_in_cfamProperty1719); 
                    match(input,K_BY,FOLLOW_K_BY_in_cfamProperty1721); 
                    match(input,102,FOLLOW_102_in_cfamProperty1723); 
                    pushFollow(FOLLOW_cfamOrdering_in_cfamProperty1725);
                    cfamOrdering(expr);

                    state._fsp--;

                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:369:56: ( ',' cfamOrdering[expr] )*
                    loop40:
                    do {
                        int alt40=2;
                        int LA40_0 = input.LA(1);

                        if ( (LA40_0==104) ) {
                            alt40=1;
                        }


                        switch (alt40) {
                    	case 1 :
                    	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:369:57: ',' cfamOrdering[expr]
                    	    {
                    	    match(input,104,FOLLOW_104_in_cfamProperty1729); 
                    	    pushFollow(FOLLOW_cfamOrdering_in_cfamProperty1731);
                    	    cfamOrdering(expr);

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop40;
                        }
                    } while (true);

                    match(input,103,FOLLOW_103_in_cfamProperty1736); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "cfamProperty"


    // $ANTLR start "cfamOrdering"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:372:1: cfamOrdering[CreateColumnFamilyStatement.RawStatement expr] : k= cident ( K_ASC | K_DESC ) ;
    public final void cfamOrdering(CreateColumnFamilyStatement.RawStatement expr) throws RecognitionException {
        ColumnIdentifier k = null;


         boolean reversed=false; 
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:374:5: (k= cident ( K_ASC | K_DESC ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:374:7: k= cident ( K_ASC | K_DESC )
            {
            pushFollow(FOLLOW_cident_in_cfamOrdering1764);
            k=cident();

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:374:16: ( K_ASC | K_DESC )
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==K_ASC) ) {
                alt42=1;
            }
            else if ( (LA42_0==K_DESC) ) {
                alt42=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;
            }
            switch (alt42) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:374:17: K_ASC
                    {
                    match(input,K_ASC,FOLLOW_K_ASC_in_cfamOrdering1767); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:374:25: K_DESC
                    {
                    match(input,K_DESC,FOLLOW_K_DESC_in_cfamOrdering1771); 
                     reversed=true;

                    }
                    break;

            }

             expr.setOrdering(k, reversed); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "cfamOrdering"


    // $ANTLR start "createIndexStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:377:1: createIndexStatement returns [CreateIndexStatement expr] : K_CREATE K_INDEX (idxName= IDENT )? K_ON cf= columnFamilyName '(' id= cident ')' ;
    public final CreateIndexStatement createIndexStatement() throws RecognitionException {
        CreateIndexStatement expr = null;

        Token idxName=null;
        CFName cf = null;

        ColumnIdentifier id = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:381:5: ( K_CREATE K_INDEX (idxName= IDENT )? K_ON cf= columnFamilyName '(' id= cident ')' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:381:7: K_CREATE K_INDEX (idxName= IDENT )? K_ON cf= columnFamilyName '(' id= cident ')'
            {
            match(input,K_CREATE,FOLLOW_K_CREATE_in_createIndexStatement1800); 
            match(input,K_INDEX,FOLLOW_K_INDEX_in_createIndexStatement1802); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:381:24: (idxName= IDENT )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==IDENT) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:381:25: idxName= IDENT
                    {
                    idxName=(Token)match(input,IDENT,FOLLOW_IDENT_in_createIndexStatement1807); 

                    }
                    break;

            }

            match(input,K_ON,FOLLOW_K_ON_in_createIndexStatement1811); 
            pushFollow(FOLLOW_columnFamilyName_in_createIndexStatement1815);
            cf=columnFamilyName();

            state._fsp--;

            match(input,102,FOLLOW_102_in_createIndexStatement1817); 
            pushFollow(FOLLOW_cident_in_createIndexStatement1821);
            id=cident();

            state._fsp--;

            match(input,103,FOLLOW_103_in_createIndexStatement1823); 
             expr = new CreateIndexStatement(cf, (idxName!=null?idxName.getText():null), id); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "createIndexStatement"


    // $ANTLR start "alterTableStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:385:1: alterTableStatement returns [AlterTableStatement expr] : K_ALTER K_COLUMNFAMILY cf= columnFamilyName ( K_ALTER id= cident K_TYPE v= comparatorType | K_ADD id= cident v= comparatorType | K_DROP id= cident | K_WITH props= properties ) ;
    public final AlterTableStatement alterTableStatement() throws RecognitionException {
        AlterTableStatement expr = null;

        CFName cf = null;

        ColumnIdentifier id = null;

        String v = null;

        Map<String, String> props = null;



                AlterTableStatement.Type type = null;
                props = new HashMap<String, String>();
            
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:396:5: ( K_ALTER K_COLUMNFAMILY cf= columnFamilyName ( K_ALTER id= cident K_TYPE v= comparatorType | K_ADD id= cident v= comparatorType | K_DROP id= cident | K_WITH props= properties ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:396:7: K_ALTER K_COLUMNFAMILY cf= columnFamilyName ( K_ALTER id= cident K_TYPE v= comparatorType | K_ADD id= cident v= comparatorType | K_DROP id= cident | K_WITH props= properties )
            {
            match(input,K_ALTER,FOLLOW_K_ALTER_in_alterTableStatement1863); 
            match(input,K_COLUMNFAMILY,FOLLOW_K_COLUMNFAMILY_in_alterTableStatement1865); 
            pushFollow(FOLLOW_columnFamilyName_in_alterTableStatement1869);
            cf=columnFamilyName();

            state._fsp--;

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:397:11: ( K_ALTER id= cident K_TYPE v= comparatorType | K_ADD id= cident v= comparatorType | K_DROP id= cident | K_WITH props= properties )
            int alt44=4;
            switch ( input.LA(1) ) {
            case K_ALTER:
                {
                alt44=1;
                }
                break;
            case K_ADD:
                {
                alt44=2;
                }
                break;
            case K_DROP:
                {
                alt44=3;
                }
                break;
            case K_WITH:
                {
                alt44=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 44, 0, input);

                throw nvae;
            }

            switch (alt44) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:397:13: K_ALTER id= cident K_TYPE v= comparatorType
                    {
                    match(input,K_ALTER,FOLLOW_K_ALTER_in_alterTableStatement1883); 
                    pushFollow(FOLLOW_cident_in_alterTableStatement1887);
                    id=cident();

                    state._fsp--;

                    match(input,K_TYPE,FOLLOW_K_TYPE_in_alterTableStatement1889); 
                    pushFollow(FOLLOW_comparatorType_in_alterTableStatement1893);
                    v=comparatorType();

                    state._fsp--;

                     type = AlterTableStatement.Type.ALTER; 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:398:13: K_ADD id= cident v= comparatorType
                    {
                    match(input,K_ADD,FOLLOW_K_ADD_in_alterTableStatement1909); 
                    pushFollow(FOLLOW_cident_in_alterTableStatement1915);
                    id=cident();

                    state._fsp--;

                    pushFollow(FOLLOW_comparatorType_in_alterTableStatement1919);
                    v=comparatorType();

                    state._fsp--;

                     type = AlterTableStatement.Type.ADD; 

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:399:13: K_DROP id= cident
                    {
                    match(input,K_DROP,FOLLOW_K_DROP_in_alterTableStatement1942); 
                    pushFollow(FOLLOW_cident_in_alterTableStatement1947);
                    id=cident();

                    state._fsp--;

                     type = AlterTableStatement.Type.DROP; 

                    }
                    break;
                case 4 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:400:13: K_WITH props= properties
                    {
                    match(input,K_WITH,FOLLOW_K_WITH_in_alterTableStatement1987); 
                    pushFollow(FOLLOW_properties_in_alterTableStatement1992);
                    props=properties();

                    state._fsp--;

                     type = AlterTableStatement.Type.OPTS; 

                    }
                    break;

            }


                    expr = new AlterTableStatement(cf, type, id, v, props);
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "alterTableStatement"


    // $ANTLR start "dropKeyspaceStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:407:1: dropKeyspaceStatement returns [DropKeyspaceStatement ksp] : K_DROP K_KEYSPACE ks= keyspaceName ;
    public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
        DropKeyspaceStatement ksp = null;

        String ks = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:411:5: ( K_DROP K_KEYSPACE ks= keyspaceName )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:411:7: K_DROP K_KEYSPACE ks= keyspaceName
            {
            match(input,K_DROP,FOLLOW_K_DROP_in_dropKeyspaceStatement2052); 
            match(input,K_KEYSPACE,FOLLOW_K_KEYSPACE_in_dropKeyspaceStatement2054); 
            pushFollow(FOLLOW_keyspaceName_in_dropKeyspaceStatement2058);
            ks=keyspaceName();

            state._fsp--;

             ksp = new DropKeyspaceStatement(ks); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ksp;
    }
    // $ANTLR end "dropKeyspaceStatement"


    // $ANTLR start "dropColumnFamilyStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:414:1: dropColumnFamilyStatement returns [DropColumnFamilyStatement stmt] : K_DROP K_COLUMNFAMILY cf= columnFamilyName ;
    public final DropColumnFamilyStatement dropColumnFamilyStatement() throws RecognitionException {
        DropColumnFamilyStatement stmt = null;

        CFName cf = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:418:5: ( K_DROP K_COLUMNFAMILY cf= columnFamilyName )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:418:7: K_DROP K_COLUMNFAMILY cf= columnFamilyName
            {
            match(input,K_DROP,FOLLOW_K_DROP_in_dropColumnFamilyStatement2083); 
            match(input,K_COLUMNFAMILY,FOLLOW_K_COLUMNFAMILY_in_dropColumnFamilyStatement2085); 
            pushFollow(FOLLOW_columnFamilyName_in_dropColumnFamilyStatement2089);
            cf=columnFamilyName();

            state._fsp--;

             stmt = new DropColumnFamilyStatement(cf); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return stmt;
    }
    // $ANTLR end "dropColumnFamilyStatement"


    // $ANTLR start "dropIndexStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:421:1: dropIndexStatement returns [DropIndexStatement expr] : K_DROP K_INDEX index= IDENT ;
    public final DropIndexStatement dropIndexStatement() throws RecognitionException {
        DropIndexStatement expr = null;

        Token index=null;

        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:425:5: ( K_DROP K_INDEX index= IDENT )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:426:7: K_DROP K_INDEX index= IDENT
            {
            match(input,K_DROP,FOLLOW_K_DROP_in_dropIndexStatement2120); 
            match(input,K_INDEX,FOLLOW_K_INDEX_in_dropIndexStatement2122); 
            index=(Token)match(input,IDENT,FOLLOW_IDENT_in_dropIndexStatement2126); 
             expr = new DropIndexStatement((index!=null?index.getText():null)); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "dropIndexStatement"


    // $ANTLR start "truncateStatement"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:430:1: truncateStatement returns [TruncateStatement stmt] : K_TRUNCATE cf= columnFamilyName ;
    public final TruncateStatement truncateStatement() throws RecognitionException {
        TruncateStatement stmt = null;

        CFName cf = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:434:5: ( K_TRUNCATE cf= columnFamilyName )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:434:7: K_TRUNCATE cf= columnFamilyName
            {
            match(input,K_TRUNCATE,FOLLOW_K_TRUNCATE_in_truncateStatement2157); 
            pushFollow(FOLLOW_columnFamilyName_in_truncateStatement2161);
            cf=columnFamilyName();

            state._fsp--;

             stmt = new TruncateStatement(cf); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return stmt;
    }
    // $ANTLR end "truncateStatement"


    // $ANTLR start "cident"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:438:1: cident returns [ColumnIdentifier id] : (t= IDENT | t= QUOTED_NAME | k= unreserved_keyword );
    public final ColumnIdentifier cident() throws RecognitionException {
        ColumnIdentifier id = null;

        Token t=null;
        String k = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:442:5: (t= IDENT | t= QUOTED_NAME | k= unreserved_keyword )
            int alt45=3;
            switch ( input.LA(1) ) {
            case IDENT:
                {
                alt45=1;
                }
                break;
            case QUOTED_NAME:
                {
                alt45=2;
                }
                break;
            case K_COUNT:
            case K_CONSISTENCY:
            case K_LEVEL:
            case K_WRITETIME:
            case K_TTL:
            case K_VALUES:
            case K_TIMESTAMP:
            case K_KEY:
            case K_COMPACT:
            case K_STORAGE:
            case K_CLUSTERING:
            case K_TYPE:
            case K_ASCII:
            case K_BIGINT:
            case K_BLOB:
            case K_BOOLEAN:
            case K_COUNTER:
            case K_DECIMAL:
            case K_DOUBLE:
            case K_FLOAT:
            case K_INT:
            case K_TEXT:
            case K_UUID:
            case K_VARCHAR:
            case K_VARINT:
            case K_TIMEUUID:
                {
                alt45=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;
            }

            switch (alt45) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:442:7: t= IDENT
                    {
                    t=(Token)match(input,IDENT,FOLLOW_IDENT_in_cident2191); 
                     id = new ColumnIdentifier((t!=null?t.getText():null), false); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:443:7: t= QUOTED_NAME
                    {
                    t=(Token)match(input,QUOTED_NAME,FOLLOW_QUOTED_NAME_in_cident2216); 
                     id = new ColumnIdentifier((t!=null?t.getText():null), true); 

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:444:7: k= unreserved_keyword
                    {
                    pushFollow(FOLLOW_unreserved_keyword_in_cident2235);
                    k=unreserved_keyword();

                    state._fsp--;

                     id = new ColumnIdentifier(k, false); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return id;
    }
    // $ANTLR end "cident"


    // $ANTLR start "keyspaceName"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:448:1: keyspaceName returns [String id] : cfOrKsName[name, true] ;
    public final String keyspaceName() throws RecognitionException {
        String id = null;

         CFName name = new CFName(); 
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:450:5: ( cfOrKsName[name, true] )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:450:7: cfOrKsName[name, true]
            {
            pushFollow(FOLLOW_cfOrKsName_in_keyspaceName2268);
            cfOrKsName(name, true);

            state._fsp--;

             id = name.getKeyspace(); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return id;
    }
    // $ANTLR end "keyspaceName"


    // $ANTLR start "columnFamilyName"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:453:1: columnFamilyName returns [CFName name] : ( cfOrKsName[name, true] '.' )? cfOrKsName[name, false] ;
    public final CFName columnFamilyName() throws RecognitionException {
        CFName name = null;

         name = new CFName(); 
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:455:5: ( ( cfOrKsName[name, true] '.' )? cfOrKsName[name, false] )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:455:7: ( cfOrKsName[name, true] '.' )? cfOrKsName[name, false]
            {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:455:7: ( cfOrKsName[name, true] '.' )?
            int alt46=2;
            switch ( input.LA(1) ) {
                case IDENT:
                    {
                    int LA46_1 = input.LA(2);

                    if ( (LA46_1==107) ) {
                        alt46=1;
                    }
                    }
                    break;
                case QUOTED_NAME:
                    {
                    int LA46_2 = input.LA(2);

                    if ( (LA46_2==107) ) {
                        alt46=1;
                    }
                    }
                    break;
                case K_COUNT:
                case K_CONSISTENCY:
                case K_LEVEL:
                case K_WRITETIME:
                case K_TTL:
                case K_VALUES:
                case K_KEY:
                case K_COMPACT:
                case K_STORAGE:
                case K_CLUSTERING:
                case K_TYPE:
                    {
                    int LA46_3 = input.LA(2);

                    if ( (LA46_3==107) ) {
                        alt46=1;
                    }
                    }
                    break;
                case K_TIMESTAMP:
                case K_ASCII:
                case K_BIGINT:
                case K_BLOB:
                case K_BOOLEAN:
                case K_COUNTER:
                case K_DECIMAL:
                case K_DOUBLE:
                case K_FLOAT:
                case K_INT:
                case K_TEXT:
                case K_UUID:
                case K_VARCHAR:
                case K_VARINT:
                case K_TIMEUUID:
                    {
                    int LA46_4 = input.LA(2);

                    if ( (LA46_4==107) ) {
                        alt46=1;
                    }
                    }
                    break;
            }

            switch (alt46) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:455:8: cfOrKsName[name, true] '.'
                    {
                    pushFollow(FOLLOW_cfOrKsName_in_columnFamilyName2302);
                    cfOrKsName(name, true);

                    state._fsp--;

                    match(input,107,FOLLOW_107_in_columnFamilyName2305); 

                    }
                    break;

            }

            pushFollow(FOLLOW_cfOrKsName_in_columnFamilyName2309);
            cfOrKsName(name, false);

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return name;
    }
    // $ANTLR end "columnFamilyName"


    // $ANTLR start "cfOrKsName"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:458:1: cfOrKsName[CFName name, boolean isKs] : (t= IDENT | t= QUOTED_NAME | k= unreserved_keyword );
    public final void cfOrKsName(CFName name, boolean isKs) throws RecognitionException {
        Token t=null;
        String k = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:459:5: (t= IDENT | t= QUOTED_NAME | k= unreserved_keyword )
            int alt47=3;
            switch ( input.LA(1) ) {
            case IDENT:
                {
                alt47=1;
                }
                break;
            case QUOTED_NAME:
                {
                alt47=2;
                }
                break;
            case K_COUNT:
            case K_CONSISTENCY:
            case K_LEVEL:
            case K_WRITETIME:
            case K_TTL:
            case K_VALUES:
            case K_TIMESTAMP:
            case K_KEY:
            case K_COMPACT:
            case K_STORAGE:
            case K_CLUSTERING:
            case K_TYPE:
            case K_ASCII:
            case K_BIGINT:
            case K_BLOB:
            case K_BOOLEAN:
            case K_COUNTER:
            case K_DECIMAL:
            case K_DOUBLE:
            case K_FLOAT:
            case K_INT:
            case K_TEXT:
            case K_UUID:
            case K_VARCHAR:
            case K_VARINT:
            case K_TIMEUUID:
                {
                alt47=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;
            }

            switch (alt47) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:459:7: t= IDENT
                    {
                    t=(Token)match(input,IDENT,FOLLOW_IDENT_in_cfOrKsName2330); 
                     if (isKs) name.setKeyspace((t!=null?t.getText():null), false); else name.setColumnFamily((t!=null?t.getText():null), false); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:460:7: t= QUOTED_NAME
                    {
                    t=(Token)match(input,QUOTED_NAME,FOLLOW_QUOTED_NAME_in_cfOrKsName2355); 
                     if (isKs) name.setKeyspace((t!=null?t.getText():null), true); else name.setColumnFamily((t!=null?t.getText():null), true); 

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:461:7: k= unreserved_keyword
                    {
                    pushFollow(FOLLOW_unreserved_keyword_in_cfOrKsName2374);
                    k=unreserved_keyword();

                    state._fsp--;

                     if (isKs) name.setKeyspace(k, false); else name.setColumnFamily(k, false); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "cfOrKsName"


    // $ANTLR start "cidentList"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:464:1: cidentList returns [List<ColumnIdentifier> items] : t1= cident ( ',' tN= cident )* ;
    public final List<ColumnIdentifier> cidentList() throws RecognitionException {
        List<ColumnIdentifier> items = null;

        ColumnIdentifier t1 = null;

        ColumnIdentifier tN = null;


         items = new ArrayList<ColumnIdentifier>(); 
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:466:5: (t1= cident ( ',' tN= cident )* )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:466:8: t1= cident ( ',' tN= cident )*
            {
            pushFollow(FOLLOW_cident_in_cidentList2408);
            t1=cident();

            state._fsp--;

             items.add(t1); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:466:38: ( ',' tN= cident )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==104) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:466:39: ',' tN= cident
            	    {
            	    match(input,104,FOLLOW_104_in_cidentList2413); 
            	    pushFollow(FOLLOW_cident_in_cidentList2417);
            	    tN=cident();

            	    state._fsp--;

            	     items.add(tN); 

            	    }
            	    break;

            	default :
            	    break loop48;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return items;
    }
    // $ANTLR end "cidentList"


    // $ANTLR start "extendedTerm"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:470:1: extendedTerm returns [Term term] : ( K_TOKEN '(' t= term ')' | t= term );
    public final Term extendedTerm() throws RecognitionException {
        Term term = null;

        Term t = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:471:5: ( K_TOKEN '(' t= term ')' | t= term )
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==K_TOKEN) ) {
                alt49=1;
            }
            else if ( (LA49_0==INTEGER||(LA49_0>=STRING_LITERAL && LA49_0<=QMARK)) ) {
                alt49=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;
            }
            switch (alt49) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:471:7: K_TOKEN '(' t= term ')'
                    {
                    match(input,K_TOKEN,FOLLOW_K_TOKEN_in_extendedTerm2443); 
                    match(input,102,FOLLOW_102_in_extendedTerm2445); 
                    pushFollow(FOLLOW_term_in_extendedTerm2449);
                    t=term();

                    state._fsp--;

                    match(input,103,FOLLOW_103_in_extendedTerm2451); 
                     term = Term.tokenOf(t); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:472:7: t= term
                    {
                    pushFollow(FOLLOW_term_in_extendedTerm2463);
                    t=term();

                    state._fsp--;

                     term = t; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return term;
    }
    // $ANTLR end "extendedTerm"


    // $ANTLR start "term"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:475:1: term returns [Term term] : (t= ( STRING_LITERAL | UUID | INTEGER | FLOAT ) | t= QMARK );
    public final Term term() throws RecognitionException {
        Term term = null;

        Token t=null;

        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:476:5: (t= ( STRING_LITERAL | UUID | INTEGER | FLOAT ) | t= QMARK )
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==INTEGER||(LA50_0>=STRING_LITERAL && LA50_0<=FLOAT)) ) {
                alt50=1;
            }
            else if ( (LA50_0==QMARK) ) {
                alt50=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }
            switch (alt50) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:476:7: t= ( STRING_LITERAL | UUID | INTEGER | FLOAT )
                    {
                    t=(Token)input.LT(1);
                    if ( input.LA(1)==INTEGER||(input.LA(1)>=STRING_LITERAL && input.LA(1)<=FLOAT) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                     term = new Term((t!=null?t.getText():null), (t!=null?t.getType():0)); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:477:7: t= QMARK
                    {
                    t=(Token)match(input,QMARK,FOLLOW_QMARK_in_term2531); 
                     term = new Term((t!=null?t.getText():null), (t!=null?t.getType():0), ++currentBindMarkerIdx); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return term;
    }
    // $ANTLR end "term"


    // $ANTLR start "intTerm"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:480:1: intTerm returns [Term integer] : (t= INTEGER | t= QMARK );
    public final Term intTerm() throws RecognitionException {
        Term integer = null;

        Token t=null;

        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:481:5: (t= INTEGER | t= QMARK )
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==INTEGER) ) {
                alt51=1;
            }
            else if ( (LA51_0==QMARK) ) {
                alt51=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                throw nvae;
            }
            switch (alt51) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:481:7: t= INTEGER
                    {
                    t=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_intTerm2593); 
                     integer = new Term((t!=null?t.getText():null), (t!=null?t.getType():0)); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:482:7: t= QMARK
                    {
                    t=(Token)match(input,QMARK,FOLLOW_QMARK_in_intTerm2605); 
                     integer = new Term((t!=null?t.getText():null), (t!=null?t.getType():0), ++currentBindMarkerIdx); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return integer;
    }
    // $ANTLR end "intTerm"


    // $ANTLR start "termPairWithOperation"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:485:1: termPairWithOperation[Map<ColumnIdentifier, Operation> columns] : key= cident '=' (value= term | c= cident ( '+' v= intTerm | (op= '-' )? v= intTerm ) ) ;
    public final void termPairWithOperation(Map<ColumnIdentifier, Operation> columns) throws RecognitionException {
        Token op=null;
        ColumnIdentifier key = null;

        Term value = null;

        ColumnIdentifier c = null;

        Term v = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:486:5: (key= cident '=' (value= term | c= cident ( '+' v= intTerm | (op= '-' )? v= intTerm ) ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:486:7: key= cident '=' (value= term | c= cident ( '+' v= intTerm | (op= '-' )? v= intTerm ) )
            {
            pushFollow(FOLLOW_cident_in_termPairWithOperation2629);
            key=cident();

            state._fsp--;

            match(input,106,FOLLOW_106_in_termPairWithOperation2631); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:487:9: (value= term | c= cident ( '+' v= intTerm | (op= '-' )? v= intTerm ) )
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==INTEGER||(LA54_0>=STRING_LITERAL && LA54_0<=QMARK)) ) {
                alt54=1;
            }
            else if ( (LA54_0==K_COUNT||(LA54_0>=K_CONSISTENCY && LA54_0<=K_LEVEL)||(LA54_0>=K_WRITETIME && LA54_0<=K_TTL)||(LA54_0>=K_VALUES && LA54_0<=K_TIMESTAMP)||(LA54_0>=K_KEY && LA54_0<=K_CLUSTERING)||LA54_0==IDENT||LA54_0==K_TYPE||LA54_0==QUOTED_NAME||(LA54_0>=K_ASCII && LA54_0<=K_TIMEUUID)) ) {
                alt54=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }
            switch (alt54) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:487:11: value= term
                    {
                    pushFollow(FOLLOW_term_in_termPairWithOperation2645);
                    value=term();

                    state._fsp--;

                     columns.put(key, new Operation(value)); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:488:11: c= cident ( '+' v= intTerm | (op= '-' )? v= intTerm )
                    {
                    pushFollow(FOLLOW_cident_in_termPairWithOperation2661);
                    c=cident();

                    state._fsp--;

                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:488:20: ( '+' v= intTerm | (op= '-' )? v= intTerm )
                    int alt53=2;
                    int LA53_0 = input.LA(1);

                    if ( (LA53_0==108) ) {
                        alt53=1;
                    }
                    else if ( (LA53_0==INTEGER||LA53_0==QMARK||LA53_0==109) ) {
                        alt53=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 53, 0, input);

                        throw nvae;
                    }
                    switch (alt53) {
                        case 1 :
                            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:488:22: '+' v= intTerm
                            {
                            match(input,108,FOLLOW_108_in_termPairWithOperation2665); 
                            pushFollow(FOLLOW_intTerm_in_termPairWithOperation2673);
                            v=intTerm();

                            state._fsp--;

                             columns.put(key, new Operation(c, Operation.Type.PLUS, v)); 

                            }
                            break;
                        case 2 :
                            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:489:22: (op= '-' )? v= intTerm
                            {
                            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:489:24: (op= '-' )?
                            int alt52=2;
                            int LA52_0 = input.LA(1);

                            if ( (LA52_0==109) ) {
                                alt52=1;
                            }
                            switch (alt52) {
                                case 1 :
                                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:489:24: op= '-'
                                    {
                                    op=(Token)match(input,109,FOLLOW_109_in_termPairWithOperation2700); 

                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_intTerm_in_termPairWithOperation2705);
                            v=intTerm();

                            state._fsp--;


                                                   validateMinusSupplied(op, v, input);
                                                   if (op == null)
                                                       v = new Term(-(Long.valueOf(v.getText())), v.getType());
                                                   columns.put(key, new Operation(c, Operation.Type.MINUS, v));
                                                 

                            }
                            break;

                    }


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "termPairWithOperation"


    // $ANTLR start "property"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:500:1: property returns [String str] : c1= cident ( ':' cn= cident )* ;
    public final String property() throws RecognitionException {
        String str = null;

        ColumnIdentifier c1 = null;

        ColumnIdentifier cn = null;


         StringBuilder sb = new StringBuilder(); 
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:502:5: (c1= cident ( ':' cn= cident )* )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:502:7: c1= cident ( ':' cn= cident )*
            {
            pushFollow(FOLLOW_cident_in_property2791);
            c1=cident();

            state._fsp--;

             sb.append(c1); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:502:36: ( ':' cn= cident )*
            loop55:
            do {
                int alt55=2;
                int LA55_0 = input.LA(1);

                if ( (LA55_0==110) ) {
                    alt55=1;
                }


                switch (alt55) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:502:38: ':' cn= cident
            	    {
            	    match(input,110,FOLLOW_110_in_property2797); 
            	    pushFollow(FOLLOW_cident_in_property2801);
            	    cn=cident();

            	    state._fsp--;

            	     sb.append(':').append(cn); 

            	    }
            	    break;

            	default :
            	    break loop55;
                }
            } while (true);

             str = sb.toString(); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return str;
    }
    // $ANTLR end "property"


    // $ANTLR start "propertyValue"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:505:1: propertyValue returns [String str] : (v= ( STRING_LITERAL | IDENT | INTEGER | FLOAT ) | u= unreserved_keyword );
    public final String propertyValue() throws RecognitionException {
        String str = null;

        Token v=null;
        String u = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:506:5: (v= ( STRING_LITERAL | IDENT | INTEGER | FLOAT ) | u= unreserved_keyword )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==INTEGER||LA56_0==IDENT||LA56_0==STRING_LITERAL||LA56_0==FLOAT) ) {
                alt56=1;
            }
            else if ( (LA56_0==K_COUNT||(LA56_0>=K_CONSISTENCY && LA56_0<=K_LEVEL)||(LA56_0>=K_WRITETIME && LA56_0<=K_TTL)||(LA56_0>=K_VALUES && LA56_0<=K_TIMESTAMP)||(LA56_0>=K_KEY && LA56_0<=K_CLUSTERING)||LA56_0==K_TYPE||(LA56_0>=K_ASCII && LA56_0<=K_TIMEUUID)) ) {
                alt56=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:506:7: v= ( STRING_LITERAL | IDENT | INTEGER | FLOAT )
                    {
                    v=(Token)input.LT(1);
                    if ( input.LA(1)==INTEGER||input.LA(1)==IDENT||input.LA(1)==STRING_LITERAL||input.LA(1)==FLOAT ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                     str = (v!=null?v.getText():null); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:507:7: u= unreserved_keyword
                    {
                    pushFollow(FOLLOW_unreserved_keyword_in_propertyValue2857);
                    u=unreserved_keyword();

                    state._fsp--;

                     str = u; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return str;
    }
    // $ANTLR end "propertyValue"


    // $ANTLR start "properties"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:510:1: properties returns [Map<String, String> props] : k1= property '=' v1= propertyValue ( K_AND kn= property '=' vn= propertyValue )* ;
    public final Map<String, String> properties() throws RecognitionException {
        Map<String, String> props = null;

        String k1 = null;

        String v1 = null;

        String kn = null;

        String vn = null;


         props = new HashMap<String, String>(); 
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:512:5: (k1= property '=' v1= propertyValue ( K_AND kn= property '=' vn= propertyValue )* )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:512:7: k1= property '=' v1= propertyValue ( K_AND kn= property '=' vn= propertyValue )*
            {
            pushFollow(FOLLOW_property_in_properties2914);
            k1=property();

            state._fsp--;

            match(input,106,FOLLOW_106_in_properties2916); 
            pushFollow(FOLLOW_propertyValue_in_properties2920);
            v1=propertyValue();

            state._fsp--;

             props.put(k1, v1); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:512:64: ( K_AND kn= property '=' vn= propertyValue )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==K_AND) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:512:65: K_AND kn= property '=' vn= propertyValue
            	    {
            	    match(input,K_AND,FOLLOW_K_AND_in_properties2925); 
            	    pushFollow(FOLLOW_property_in_properties2929);
            	    kn=property();

            	    state._fsp--;

            	    match(input,106,FOLLOW_106_in_properties2931); 
            	    pushFollow(FOLLOW_propertyValue_in_properties2935);
            	    vn=propertyValue();

            	    state._fsp--;

            	     props.put(kn, vn); 

            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return props;
    }
    // $ANTLR end "properties"


    // $ANTLR start "relation"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:515:1: relation returns [Relation rel] : (name= cident type= ( '=' | '<' | '<=' | '>=' | '>' ) t= term | K_TOKEN '(' name= cident ')' type= ( '=' | '<' | '<=' | '>=' | '>' ) t= extendedTerm | name= cident K_IN '(' f1= term ( ',' fN= term )* ')' );
    public final Relation relation() throws RecognitionException {
        Relation rel = null;

        Token type=null;
        ColumnIdentifier name = null;

        Term t = null;

        Term f1 = null;

        Term fN = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:516:5: (name= cident type= ( '=' | '<' | '<=' | '>=' | '>' ) t= term | K_TOKEN '(' name= cident ')' type= ( '=' | '<' | '<=' | '>=' | '>' ) t= extendedTerm | name= cident K_IN '(' f1= term ( ',' fN= term )* ')' )
            int alt59=3;
            switch ( input.LA(1) ) {
            case IDENT:
                {
                int LA59_1 = input.LA(2);

                if ( (LA59_1==106||(LA59_1>=111 && LA59_1<=114)) ) {
                    alt59=1;
                }
                else if ( (LA59_1==K_IN) ) {
                    alt59=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 1, input);

                    throw nvae;
                }
                }
                break;
            case QUOTED_NAME:
                {
                int LA59_2 = input.LA(2);

                if ( (LA59_2==K_IN) ) {
                    alt59=3;
                }
                else if ( (LA59_2==106||(LA59_2>=111 && LA59_2<=114)) ) {
                    alt59=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 2, input);

                    throw nvae;
                }
                }
                break;
            case K_COUNT:
            case K_CONSISTENCY:
            case K_LEVEL:
            case K_WRITETIME:
            case K_TTL:
            case K_VALUES:
            case K_KEY:
            case K_COMPACT:
            case K_STORAGE:
            case K_CLUSTERING:
            case K_TYPE:
                {
                int LA59_3 = input.LA(2);

                if ( (LA59_3==106||(LA59_3>=111 && LA59_3<=114)) ) {
                    alt59=1;
                }
                else if ( (LA59_3==K_IN) ) {
                    alt59=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 3, input);

                    throw nvae;
                }
                }
                break;
            case K_TIMESTAMP:
            case K_ASCII:
            case K_BIGINT:
            case K_BLOB:
            case K_BOOLEAN:
            case K_COUNTER:
            case K_DECIMAL:
            case K_DOUBLE:
            case K_FLOAT:
            case K_INT:
            case K_TEXT:
            case K_UUID:
            case K_VARCHAR:
            case K_VARINT:
            case K_TIMEUUID:
                {
                int LA59_4 = input.LA(2);

                if ( (LA59_4==106||(LA59_4>=111 && LA59_4<=114)) ) {
                    alt59=1;
                }
                else if ( (LA59_4==K_IN) ) {
                    alt59=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 4, input);

                    throw nvae;
                }
                }
                break;
            case K_TOKEN:
                {
                alt59=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 59, 0, input);

                throw nvae;
            }

            switch (alt59) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:516:7: name= cident type= ( '=' | '<' | '<=' | '>=' | '>' ) t= term
                    {
                    pushFollow(FOLLOW_cident_in_relation2963);
                    name=cident();

                    state._fsp--;

                    type=(Token)input.LT(1);
                    if ( input.LA(1)==106||(input.LA(1)>=111 && input.LA(1)<=114) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_term_in_relation2989);
                    t=term();

                    state._fsp--;

                     rel = new Relation(name, (type!=null?type.getText():null), t); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:517:7: K_TOKEN '(' name= cident ')' type= ( '=' | '<' | '<=' | '>=' | '>' ) t= extendedTerm
                    {
                    match(input,K_TOKEN,FOLLOW_K_TOKEN_in_relation2999); 
                    match(input,102,FOLLOW_102_in_relation3001); 
                    pushFollow(FOLLOW_cident_in_relation3005);
                    name=cident();

                    state._fsp--;

                    match(input,103,FOLLOW_103_in_relation3007); 
                    type=(Token)input.LT(1);
                    if ( input.LA(1)==106||(input.LA(1)>=111 && input.LA(1)<=114) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_extendedTerm_in_relation3032);
                    t=extendedTerm();

                    state._fsp--;

                     rel = new Relation(name, (type!=null?type.getText():null), t, true); 

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:518:7: name= cident K_IN '(' f1= term ( ',' fN= term )* ')'
                    {
                    pushFollow(FOLLOW_cident_in_relation3044);
                    name=cident();

                    state._fsp--;

                    match(input,K_IN,FOLLOW_K_IN_in_relation3046); 
                     rel = Relation.createInRelation(name); 
                    match(input,102,FOLLOW_102_in_relation3056); 
                    pushFollow(FOLLOW_term_in_relation3060);
                    f1=term();

                    state._fsp--;

                     rel.addInValue(f1); 
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:519:44: ( ',' fN= term )*
                    loop58:
                    do {
                        int alt58=2;
                        int LA58_0 = input.LA(1);

                        if ( (LA58_0==104) ) {
                            alt58=1;
                        }


                        switch (alt58) {
                    	case 1 :
                    	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:519:45: ',' fN= term
                    	    {
                    	    match(input,104,FOLLOW_104_in_relation3065); 
                    	    pushFollow(FOLLOW_term_in_relation3069);
                    	    fN=term();

                    	    state._fsp--;

                    	     rel.addInValue(fN); 

                    	    }
                    	    break;

                    	default :
                    	    break loop58;
                        }
                    } while (true);

                    match(input,103,FOLLOW_103_in_relation3076); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return rel;
    }
    // $ANTLR end "relation"


    // $ANTLR start "comparatorType"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:522:1: comparatorType returns [String str] : (c= native_type | s= STRING_LITERAL );
    public final String comparatorType() throws RecognitionException {
        String str = null;

        Token s=null;
        String c = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:523:5: (c= native_type | s= STRING_LITERAL )
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==K_TIMESTAMP||(LA60_0>=K_ASCII && LA60_0<=K_TIMEUUID)) ) {
                alt60=1;
            }
            else if ( (LA60_0==STRING_LITERAL) ) {
                alt60=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 60, 0, input);

                throw nvae;
            }
            switch (alt60) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:523:7: c= native_type
                    {
                    pushFollow(FOLLOW_native_type_in_comparatorType3099);
                    c=native_type();

                    state._fsp--;

                     str =c; 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:524:7: s= STRING_LITERAL
                    {
                    s=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_comparatorType3114); 
                     str = (s!=null?s.getText():null); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return str;
    }
    // $ANTLR end "comparatorType"


    // $ANTLR start "native_type"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:527:1: native_type returns [String str] : c= ( K_ASCII | K_BIGINT | K_BLOB | K_BOOLEAN | K_COUNTER | K_DECIMAL | K_DOUBLE | K_FLOAT | K_INT | K_TEXT | K_TIMESTAMP | K_UUID | K_VARCHAR | K_VARINT | K_TIMEUUID ) ;
    public final String native_type() throws RecognitionException {
        String str = null;

        Token c=null;

        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:528:5: (c= ( K_ASCII | K_BIGINT | K_BLOB | K_BOOLEAN | K_COUNTER | K_DECIMAL | K_DOUBLE | K_FLOAT | K_INT | K_TEXT | K_TIMESTAMP | K_UUID | K_VARCHAR | K_VARINT | K_TIMEUUID ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:528:7: c= ( K_ASCII | K_BIGINT | K_BLOB | K_BOOLEAN | K_COUNTER | K_DECIMAL | K_DOUBLE | K_FLOAT | K_INT | K_TEXT | K_TIMESTAMP | K_UUID | K_VARCHAR | K_VARINT | K_TIMEUUID )
            {
            c=(Token)input.LT(1);
            if ( input.LA(1)==K_TIMESTAMP||(input.LA(1)>=K_ASCII && input.LA(1)<=K_TIMEUUID) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

             return (c!=null?c.getText():null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return str;
    }
    // $ANTLR end "native_type"


    // $ANTLR start "unreserved_keyword"
    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:546:1: unreserved_keyword returns [String str] : (k= ( K_KEY | K_CONSISTENCY | K_CLUSTERING | K_LEVEL | K_COUNT | K_TTL | K_COMPACT | K_STORAGE | K_TYPE | K_VALUES | K_WRITETIME ) | t= native_type );
    public final String unreserved_keyword() throws RecognitionException {
        String str = null;

        Token k=null;
        String t = null;


        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:547:5: (k= ( K_KEY | K_CONSISTENCY | K_CLUSTERING | K_LEVEL | K_COUNT | K_TTL | K_COMPACT | K_STORAGE | K_TYPE | K_VALUES | K_WRITETIME ) | t= native_type )
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==K_COUNT||(LA61_0>=K_CONSISTENCY && LA61_0<=K_LEVEL)||(LA61_0>=K_WRITETIME && LA61_0<=K_TTL)||LA61_0==K_VALUES||(LA61_0>=K_KEY && LA61_0<=K_CLUSTERING)||LA61_0==K_TYPE) ) {
                alt61=1;
            }
            else if ( (LA61_0==K_TIMESTAMP||(LA61_0>=K_ASCII && LA61_0<=K_TIMEUUID)) ) {
                alt61=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 61, 0, input);

                throw nvae;
            }
            switch (alt61) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:547:7: k= ( K_KEY | K_CONSISTENCY | K_CLUSTERING | K_LEVEL | K_COUNT | K_TTL | K_COMPACT | K_STORAGE | K_TYPE | K_VALUES | K_WRITETIME )
                    {
                    k=(Token)input.LT(1);
                    if ( input.LA(1)==K_COUNT||(input.LA(1)>=K_CONSISTENCY && input.LA(1)<=K_LEVEL)||(input.LA(1)>=K_WRITETIME && input.LA(1)<=K_TTL)||input.LA(1)==K_VALUES||(input.LA(1)>=K_KEY && input.LA(1)<=K_CLUSTERING)||input.LA(1)==K_TYPE ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                     str = (k!=null?k.getText():null); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:559:7: t= native_type
                    {
                    pushFollow(FOLLOW_native_type_in_unreserved_keyword3486);
                    t=native_type();

                    state._fsp--;

                     str = t; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return str;
    }
    // $ANTLR end "unreserved_keyword"

    // Delegated rules


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\21\uffff";
    static final String DFA2_eofS =
        "\21\uffff";
    static final String DFA2_minS =
        "\1\4\7\uffff\2\40\7\uffff";
    static final String DFA2_maxS =
        "\1\57\7\uffff\2\50\7\uffff";
    static final String DFA2_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\2\uffff\1\16\1\10\1\11\1\12"+
        "\1\13\1\14\1\15";
    static final String DFA2_specialS =
        "\21\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\6\1\1\17\uffff\1\2\3\uffff\1\3\1\uffff\1\5\1\4\2\uffff\1"+
            "\10\13\uffff\1\12\2\uffff\1\11\1\7",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\13\1\uffff\1\14\5\uffff\1\15",
            "\1\16\1\uffff\1\17\5\uffff\1\20",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "127:1: cqlStatement returns [ParsedStatement stmt] : (st1= selectStatement | st2= insertStatement | st3= updateStatement | st4= batchStatement | st5= deleteStatement | st6= useStatement | st7= truncateStatement | st8= createKeyspaceStatement | st9= createColumnFamilyStatement | st10= createIndexStatement | st11= dropKeyspaceStatement | st12= dropColumnFamilyStatement | st13= dropIndexStatement | st14= alterTableStatement );";
        }
    }
 

    public static final BitSet FOLLOW_cqlStatement_in_query72 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_101_in_query75 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_EOF_in_query79 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectStatement_in_cqlStatement113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_insertStatement_in_cqlStatement138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_updateStatement_in_cqlStatement163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_batchStatement_in_cqlStatement188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_deleteStatement_in_cqlStatement214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_useStatement_in_cqlStatement239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncateStatement_in_cqlStatement267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createKeyspaceStatement_in_cqlStatement290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createColumnFamilyStatement_in_cqlStatement307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createIndexStatement_in_cqlStatement319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dropKeyspaceStatement_in_cqlStatement338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dropColumnFamilyStatement_in_cqlStatement356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dropIndexStatement_in_cqlStatement370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_alterTableStatement_in_cqlStatement391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_USE_in_useStatement424 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_keyspaceName_in_useStatement428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_SELECT_in_selectStatement462 = new BitSet(new long[]{0xFF8112F001830640L,0x000002000000001FL});
    public static final BitSet FOLLOW_selectClause_in_selectStatement468 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_K_COUNT_in_selectStatement473 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_selectStatement475 = new BitSet(new long[]{0xFF8112F001838640L,0x000002000000001FL});
    public static final BitSet FOLLOW_selectCountClause_in_selectStatement479 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_selectStatement481 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_K_FROM_in_selectStatement494 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_columnFamilyName_in_selectStatement498 = new BitSet(new long[]{0x0000000000005902L});
    public static final BitSet FOLLOW_K_USING_in_selectStatement508 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_K_CONSISTENCY_in_selectStatement510 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_K_LEVEL_in_selectStatement512 = new BitSet(new long[]{0x0000000000005802L});
    public static final BitSet FOLLOW_K_WHERE_in_selectStatement527 = new BitSet(new long[]{0xFF8312F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_whereClause_in_selectStatement531 = new BitSet(new long[]{0x0000000000005002L});
    public static final BitSet FOLLOW_K_ORDER_in_selectStatement544 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_K_BY_in_selectStatement546 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_orderByClause_in_selectStatement548 = new BitSet(new long[]{0x0000000000004002L,0x0000010000000000L});
    public static final BitSet FOLLOW_104_in_selectStatement553 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_orderByClause_in_selectStatement555 = new BitSet(new long[]{0x0000000000004002L,0x0000010000000000L});
    public static final BitSet FOLLOW_K_LIMIT_in_selectStatement572 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_INTEGER_in_selectStatement576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selector_in_selectClause612 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_104_in_selectClause617 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_selector_in_selectClause621 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_105_in_selectClause633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cident_in_selector658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_WRITETIME_in_selector680 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_selector682 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_selector686 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_selector688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_TTL_in_selector698 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_selector700 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_selector704 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_selector706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cidentList_in_selectCountClause737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_selectCountClause747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_selectCountClause769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_relation_in_whereClause807 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_K_AND_in_whereClause812 = new BitSet(new long[]{0xFF8312F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_relation_in_whereClause816 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_cident_in_orderByClause848 = new BitSet(new long[]{0x0000000000180002L});
    public static final BitSet FOLLOW_K_ASC_in_orderByClause853 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_DESC_in_orderByClause857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_INSERT_in_insertStatement895 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_K_INTO_in_insertStatement897 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_columnFamilyName_in_insertStatement901 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_insertStatement913 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_insertStatement917 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_104_in_insertStatement924 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_insertStatement928 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_103_in_insertStatement935 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_K_VALUES_in_insertStatement945 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_insertStatement957 = new BitSet(new long[]{0x003C000000008000L});
    public static final BitSet FOLLOW_term_in_insertStatement961 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_104_in_insertStatement967 = new BitSet(new long[]{0x003C000000008000L});
    public static final BitSet FOLLOW_term_in_insertStatement971 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_103_in_insertStatement978 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_usingClause_in_insertStatement990 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_USING_in_usingClause1020 = new BitSet(new long[]{0x0000000001020200L});
    public static final BitSet FOLLOW_usingClauseObjective_in_usingClause1022 = new BitSet(new long[]{0x0000000001060202L});
    public static final BitSet FOLLOW_K_AND_in_usingClause1027 = new BitSet(new long[]{0x0000000001020200L});
    public static final BitSet FOLLOW_usingClauseObjective_in_usingClause1030 = new BitSet(new long[]{0x0000000001060202L});
    public static final BitSet FOLLOW_K_USING_in_usingClauseDelete1052 = new BitSet(new long[]{0x0000000001000200L});
    public static final BitSet FOLLOW_usingClauseDeleteObjective_in_usingClauseDelete1054 = new BitSet(new long[]{0x0000000001040202L});
    public static final BitSet FOLLOW_K_AND_in_usingClauseDelete1059 = new BitSet(new long[]{0x0000000001000200L});
    public static final BitSet FOLLOW_usingClauseDeleteObjective_in_usingClauseDelete1062 = new BitSet(new long[]{0x0000000001040202L});
    public static final BitSet FOLLOW_K_CONSISTENCY_in_usingClauseDeleteObjective1084 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_K_LEVEL_in_usingClauseDeleteObjective1086 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_TIMESTAMP_in_usingClauseDeleteObjective1097 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_INTEGER_in_usingClauseDeleteObjective1101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_usingClauseDeleteObjective_in_usingClauseObjective1121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_TTL_in_usingClauseObjective1130 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_INTEGER_in_usingClauseObjective1134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_UPDATE_in_updateStatement1168 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_columnFamilyName_in_updateStatement1172 = new BitSet(new long[]{0x0000000004000100L});
    public static final BitSet FOLLOW_usingClause_in_updateStatement1182 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_K_SET_in_updateStatement1194 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_termPairWithOperation_in_updateStatement1196 = new BitSet(new long[]{0x0000000000000800L,0x0000010000000000L});
    public static final BitSet FOLLOW_104_in_updateStatement1200 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_termPairWithOperation_in_updateStatement1202 = new BitSet(new long[]{0x0000000000000800L,0x0000010000000000L});
    public static final BitSet FOLLOW_K_WHERE_in_updateStatement1213 = new BitSet(new long[]{0xFF8312F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_whereClause_in_updateStatement1217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_DELETE_in_deleteStatement1257 = new BitSet(new long[]{0xFF8112F0018306C0L,0x000000000000001FL});
    public static final BitSet FOLLOW_cidentList_in_deleteStatement1263 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_K_FROM_in_deleteStatement1276 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_columnFamilyName_in_deleteStatement1280 = new BitSet(new long[]{0x0000000000000900L});
    public static final BitSet FOLLOW_usingClauseDelete_in_deleteStatement1290 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_K_WHERE_in_deleteStatement1302 = new BitSet(new long[]{0xFF8312F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_whereClause_in_deleteStatement1306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_BEGIN_in_batchStatement1347 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_K_BATCH_in_batchStatement1349 = new BitSet(new long[]{0x000000000A200100L});
    public static final BitSet FOLLOW_usingClause_in_batchStatement1353 = new BitSet(new long[]{0x000000000A200100L});
    public static final BitSet FOLLOW_batchStatementObjective_in_batchStatement1371 = new BitSet(new long[]{0x000000004A200100L,0x0000002000000000L});
    public static final BitSet FOLLOW_101_in_batchStatement1373 = new BitSet(new long[]{0x000000004A200100L});
    public static final BitSet FOLLOW_batchStatementObjective_in_batchStatement1382 = new BitSet(new long[]{0x000000004A200100L,0x0000002000000000L});
    public static final BitSet FOLLOW_101_in_batchStatement1384 = new BitSet(new long[]{0x000000004A200100L});
    public static final BitSet FOLLOW_K_APPLY_in_batchStatement1398 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_K_BATCH_in_batchStatement1400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_insertStatement_in_batchStatementObjective1431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_updateStatement_in_batchStatementObjective1444 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_deleteStatement_in_batchStatementObjective1457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_CREATE_in_createKeyspaceStatement1483 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_K_KEYSPACE_in_createKeyspaceStatement1485 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_keyspaceName_in_createKeyspaceStatement1489 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_K_WITH_in_createKeyspaceStatement1497 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_properties_in_createKeyspaceStatement1501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_CREATE_in_createColumnFamilyStatement1526 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_K_COLUMNFAMILY_in_createColumnFamilyStatement1528 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_columnFamilyName_in_createColumnFamilyStatement1532 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_cfamDefinition_in_createColumnFamilyStatement1542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_102_in_cfamDefinition1561 = new BitSet(new long[]{0xFF8112F801830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cfamColumns_in_cfamDefinition1563 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_104_in_cfamDefinition1568 = new BitSet(new long[]{0xFF8112F801830640L,0x000001800000001FL});
    public static final BitSet FOLLOW_cfamColumns_in_cfamDefinition1570 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_103_in_cfamDefinition1577 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_K_WITH_in_cfamDefinition1587 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cfamProperty_in_cfamDefinition1589 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_K_AND_in_cfamDefinition1594 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cfamProperty_in_cfamDefinition1596 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_cident_in_cfamColumns1622 = new BitSet(new long[]{0xFF8512F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_comparatorType_in_cfamColumns1626 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_K_PRIMARY_in_cfamColumns1631 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_K_KEY_in_cfamColumns1633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_PRIMARY_in_cfamColumns1645 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_K_KEY_in_cfamColumns1647 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_cfamColumns1649 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_cfamColumns1653 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_104_in_cfamColumns1658 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_cfamColumns1662 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_103_in_cfamColumns1669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_property_in_cfamProperty1689 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
    public static final BitSet FOLLOW_106_in_cfamProperty1691 = new BitSet(new long[]{0xFF9512F001838640L,0x000000000000001FL});
    public static final BitSet FOLLOW_propertyValue_in_cfamProperty1695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_COMPACT_in_cfamProperty1705 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_K_STORAGE_in_cfamProperty1707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_CLUSTERING_in_cfamProperty1717 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_K_ORDER_in_cfamProperty1719 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_K_BY_in_cfamProperty1721 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_cfamProperty1723 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cfamOrdering_in_cfamProperty1725 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_104_in_cfamProperty1729 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cfamOrdering_in_cfamProperty1731 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_103_in_cfamProperty1736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cident_in_cfamOrdering1764 = new BitSet(new long[]{0x0000000000180000L});
    public static final BitSet FOLLOW_K_ASC_in_cfamOrdering1767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_DESC_in_cfamOrdering1771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_CREATE_in_createIndexStatement1800 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_K_INDEX_in_createIndexStatement1802 = new BitSet(new long[]{0x0000060000000000L});
    public static final BitSet FOLLOW_IDENT_in_createIndexStatement1807 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_K_ON_in_createIndexStatement1811 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_columnFamilyName_in_createIndexStatement1815 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_createIndexStatement1817 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_createIndexStatement1821 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_createIndexStatement1823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_ALTER_in_alterTableStatement1863 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_K_COLUMNFAMILY_in_alterTableStatement1865 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_columnFamilyName_in_alterTableStatement1869 = new BitSet(new long[]{0x0000680200000000L});
    public static final BitSet FOLLOW_K_ALTER_in_alterTableStatement1883 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_alterTableStatement1887 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_K_TYPE_in_alterTableStatement1889 = new BitSet(new long[]{0xFF8512F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_comparatorType_in_alterTableStatement1893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_ADD_in_alterTableStatement1909 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_alterTableStatement1915 = new BitSet(new long[]{0xFF8512F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_comparatorType_in_alterTableStatement1919 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_DROP_in_alterTableStatement1942 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_alterTableStatement1947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_WITH_in_alterTableStatement1987 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_properties_in_alterTableStatement1992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_DROP_in_dropKeyspaceStatement2052 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_K_KEYSPACE_in_dropKeyspaceStatement2054 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_keyspaceName_in_dropKeyspaceStatement2058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_DROP_in_dropColumnFamilyStatement2083 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_K_COLUMNFAMILY_in_dropColumnFamilyStatement2085 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_columnFamilyName_in_dropColumnFamilyStatement2089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_DROP_in_dropIndexStatement2120 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_K_INDEX_in_dropIndexStatement2122 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_IDENT_in_dropIndexStatement2126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_TRUNCATE_in_truncateStatement2157 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_columnFamilyName_in_truncateStatement2161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_cident2191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTED_NAME_in_cident2216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unreserved_keyword_in_cident2235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cfOrKsName_in_keyspaceName2268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cfOrKsName_in_columnFamilyName2302 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_107_in_columnFamilyName2305 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cfOrKsName_in_columnFamilyName2309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_cfOrKsName2330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTED_NAME_in_cfOrKsName2355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unreserved_keyword_in_cfOrKsName2374 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cident_in_cidentList2408 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_104_in_cidentList2413 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_cidentList2417 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_K_TOKEN_in_extendedTerm2443 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_extendedTerm2445 = new BitSet(new long[]{0x003C000000008000L});
    public static final BitSet FOLLOW_term_in_extendedTerm2449 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_extendedTerm2451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_extendedTerm2463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_term2504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QMARK_in_term2531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_intTerm2593 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QMARK_in_intTerm2605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cident_in_termPairWithOperation2629 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
    public static final BitSet FOLLOW_106_in_termPairWithOperation2631 = new BitSet(new long[]{0xFFBD12F001838640L,0x000000000000001FL});
    public static final BitSet FOLLOW_term_in_termPairWithOperation2645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cident_in_termPairWithOperation2661 = new BitSet(new long[]{0x0020000000008000L,0x0000300000000000L});
    public static final BitSet FOLLOW_108_in_termPairWithOperation2665 = new BitSet(new long[]{0x0020000000008000L,0x0000300000000000L});
    public static final BitSet FOLLOW_intTerm_in_termPairWithOperation2673 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_109_in_termPairWithOperation2700 = new BitSet(new long[]{0x0020000000008000L,0x0000300000000000L});
    public static final BitSet FOLLOW_intTerm_in_termPairWithOperation2705 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cident_in_property2791 = new BitSet(new long[]{0x0000000000000002L,0x0000400000000000L});
    public static final BitSet FOLLOW_110_in_property2797 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_property2801 = new BitSet(new long[]{0x0000000000000002L,0x0000400000000000L});
    public static final BitSet FOLLOW_set_in_propertyValue2831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unreserved_keyword_in_propertyValue2857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_property_in_properties2914 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
    public static final BitSet FOLLOW_106_in_properties2916 = new BitSet(new long[]{0xFF9512F001838640L,0x000000000000001FL});
    public static final BitSet FOLLOW_propertyValue_in_properties2920 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_K_AND_in_properties2925 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_property_in_properties2929 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
    public static final BitSet FOLLOW_106_in_properties2931 = new BitSet(new long[]{0xFF9512F001838640L,0x000000000000001FL});
    public static final BitSet FOLLOW_propertyValue_in_properties2935 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_cident_in_relation2963 = new BitSet(new long[]{0x0000000000000000L,0x0007840000000000L});
    public static final BitSet FOLLOW_set_in_relation2967 = new BitSet(new long[]{0x003C000000008000L});
    public static final BitSet FOLLOW_term_in_relation2989 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_K_TOKEN_in_relation2999 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_relation3001 = new BitSet(new long[]{0xFF8112F001830640L,0x000000000000001FL});
    public static final BitSet FOLLOW_cident_in_relation3005 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_relation3007 = new BitSet(new long[]{0x0000000000000000L,0x0007840000000000L});
    public static final BitSet FOLLOW_set_in_relation3011 = new BitSet(new long[]{0x003E000000008000L});
    public static final BitSet FOLLOW_extendedTerm_in_relation3032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cident_in_relation3044 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_K_IN_in_relation3046 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_102_in_relation3056 = new BitSet(new long[]{0x003C000000008000L});
    public static final BitSet FOLLOW_term_in_relation3060 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_104_in_relation3065 = new BitSet(new long[]{0x003C000000008000L});
    public static final BitSet FOLLOW_term_in_relation3069 = new BitSet(new long[]{0x0000000000000000L,0x0000018000000000L});
    public static final BitSet FOLLOW_103_in_relation3076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_native_type_in_comparatorType3099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_comparatorType3114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_native_type3139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_unreserved_keyword3342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_native_type_in_unreserved_keyword3486 = new BitSet(new long[]{0x0000000000000002L});

}