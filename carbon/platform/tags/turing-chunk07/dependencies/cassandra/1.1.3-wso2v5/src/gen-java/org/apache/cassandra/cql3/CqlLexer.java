// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g 2013-08-23 16:21:04

    package org.apache.cassandra.cql3;

    import org.apache.cassandra.thrift.InvalidRequestException;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CqlLexer extends Lexer {
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
    public static final int K_TYPE=44;
    public static final int K_KEYSPACE=32;
    public static final int K_COUNT=6;
    public static final int A=80;
    public static final int B=90;
    public static final int C=72;
    public static final int L=71;
    public static final int M=77;
    public static final int N=81;
    public static final int O=76;
    public static final int H=79;
    public static final int I=85;
    public static final int K_UPDATE=25;
    public static final int J=93;
    public static final int K=83;
    public static final int U=86;
    public static final int T=73;
    public static final int K_TEXT=64;
    public static final int W=78;
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
    public static final int K_VARCHAR=66;
    public static final int T__110=110;
    public static final int IDENT=41;
    public static final int T__113=113;
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

        List<Token> tokens = new ArrayList<Token>();

        public void emit(Token token)
        {
            state.token = token;
            tokens.add(token);
        }

        public Token nextToken()
        {
            super.nextToken();
            if (tokens.size() == 0)
                return Token.EOF_TOKEN;
            return tokens.remove(0);
        }

        private List<String> recognitionErrors = new ArrayList<String>();

        public void displayRecognitionError(String[] tokenNames, RecognitionException e)
        {
            String hdr = getErrorHeader(e);
            String msg = getErrorMessage(e, tokenNames);
            recognitionErrors.add(hdr + " " + msg);
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


    // delegates
    // delegators

    public CqlLexer() {;} 
    public CqlLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CqlLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g"; }

    // $ANTLR start "T__101"
    public final void mT__101() throws RecognitionException {
        try {
            int _type = T__101;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:50:8: ( ';' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:50:10: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__101"

    // $ANTLR start "T__102"
    public final void mT__102() throws RecognitionException {
        try {
            int _type = T__102;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:51:8: ( '(' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:51:10: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__102"

    // $ANTLR start "T__103"
    public final void mT__103() throws RecognitionException {
        try {
            int _type = T__103;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:52:8: ( ')' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:52:10: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__103"

    // $ANTLR start "T__104"
    public final void mT__104() throws RecognitionException {
        try {
            int _type = T__104;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:53:8: ( ',' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:53:10: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__104"

    // $ANTLR start "T__105"
    public final void mT__105() throws RecognitionException {
        try {
            int _type = T__105;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:54:8: ( '\\*' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:54:10: '\\*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__105"

    // $ANTLR start "T__106"
    public final void mT__106() throws RecognitionException {
        try {
            int _type = T__106;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:55:8: ( '=' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:55:10: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__106"

    // $ANTLR start "T__107"
    public final void mT__107() throws RecognitionException {
        try {
            int _type = T__107;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:56:8: ( '.' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:56:10: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__107"

    // $ANTLR start "T__108"
    public final void mT__108() throws RecognitionException {
        try {
            int _type = T__108;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:57:8: ( '+' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:57:10: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__108"

    // $ANTLR start "T__109"
    public final void mT__109() throws RecognitionException {
        try {
            int _type = T__109;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:58:8: ( '-' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:58:10: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__109"

    // $ANTLR start "T__110"
    public final void mT__110() throws RecognitionException {
        try {
            int _type = T__110;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:59:8: ( ':' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:59:10: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__110"

    // $ANTLR start "T__111"
    public final void mT__111() throws RecognitionException {
        try {
            int _type = T__111;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:60:8: ( '<' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:60:10: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__111"

    // $ANTLR start "T__112"
    public final void mT__112() throws RecognitionException {
        try {
            int _type = T__112;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:61:8: ( '<=' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:61:10: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__112"

    // $ANTLR start "T__113"
    public final void mT__113() throws RecognitionException {
        try {
            int _type = T__113;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:62:8: ( '>=' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:62:10: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__113"

    // $ANTLR start "T__114"
    public final void mT__114() throws RecognitionException {
        try {
            int _type = T__114;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:63:8: ( '>' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:63:10: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__114"

    // $ANTLR start "K_SELECT"
    public final void mK_SELECT() throws RecognitionException {
        try {
            int _type = K_SELECT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:564:9: ( S E L E C T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:564:16: S E L E C T
            {
            mS(); 
            mE(); 
            mL(); 
            mE(); 
            mC(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_SELECT"

    // $ANTLR start "K_FROM"
    public final void mK_FROM() throws RecognitionException {
        try {
            int _type = K_FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:565:7: ( F R O M )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:565:16: F R O M
            {
            mF(); 
            mR(); 
            mO(); 
            mM(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_FROM"

    // $ANTLR start "K_WHERE"
    public final void mK_WHERE() throws RecognitionException {
        try {
            int _type = K_WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:566:8: ( W H E R E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:566:16: W H E R E
            {
            mW(); 
            mH(); 
            mE(); 
            mR(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_WHERE"

    // $ANTLR start "K_AND"
    public final void mK_AND() throws RecognitionException {
        try {
            int _type = K_AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:567:6: ( A N D )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:567:16: A N D
            {
            mA(); 
            mN(); 
            mD(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_AND"

    // $ANTLR start "K_KEY"
    public final void mK_KEY() throws RecognitionException {
        try {
            int _type = K_KEY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:568:6: ( K E Y )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:568:16: K E Y
            {
            mK(); 
            mE(); 
            mY(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_KEY"

    // $ANTLR start "K_INSERT"
    public final void mK_INSERT() throws RecognitionException {
        try {
            int _type = K_INSERT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:569:9: ( I N S E R T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:569:16: I N S E R T
            {
            mI(); 
            mN(); 
            mS(); 
            mE(); 
            mR(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_INSERT"

    // $ANTLR start "K_UPDATE"
    public final void mK_UPDATE() throws RecognitionException {
        try {
            int _type = K_UPDATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:570:9: ( U P D A T E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:570:16: U P D A T E
            {
            mU(); 
            mP(); 
            mD(); 
            mA(); 
            mT(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_UPDATE"

    // $ANTLR start "K_WITH"
    public final void mK_WITH() throws RecognitionException {
        try {
            int _type = K_WITH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:571:7: ( W I T H )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:571:16: W I T H
            {
            mW(); 
            mI(); 
            mT(); 
            mH(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_WITH"

    // $ANTLR start "K_LIMIT"
    public final void mK_LIMIT() throws RecognitionException {
        try {
            int _type = K_LIMIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:572:8: ( L I M I T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:572:16: L I M I T
            {
            mL(); 
            mI(); 
            mM(); 
            mI(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_LIMIT"

    // $ANTLR start "K_USING"
    public final void mK_USING() throws RecognitionException {
        try {
            int _type = K_USING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:573:8: ( U S I N G )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:573:16: U S I N G
            {
            mU(); 
            mS(); 
            mI(); 
            mN(); 
            mG(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_USING"

    // $ANTLR start "K_CONSISTENCY"
    public final void mK_CONSISTENCY() throws RecognitionException {
        try {
            int _type = K_CONSISTENCY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:574:14: ( C O N S I S T E N C Y )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:574:16: C O N S I S T E N C Y
            {
            mC(); 
            mO(); 
            mN(); 
            mS(); 
            mI(); 
            mS(); 
            mT(); 
            mE(); 
            mN(); 
            mC(); 
            mY(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_CONSISTENCY"

    // $ANTLR start "K_LEVEL"
    public final void mK_LEVEL() throws RecognitionException {
        try {
            int _type = K_LEVEL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:575:8: ( ( O N E | Q U O R U M | A L L | A N Y | L O C A L '_' Q U O R U M | E A C H '_' Q U O R U M | T W O | T H R E E ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:575:16: ( O N E | Q U O R U M | A L L | A N Y | L O C A L '_' Q U O R U M | E A C H '_' Q U O R U M | T W O | T H R E E )
            {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:575:16: ( O N E | Q U O R U M | A L L | A N Y | L O C A L '_' Q U O R U M | E A C H '_' Q U O R U M | T W O | T H R E E )
            int alt1=8;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:575:18: O N E
                    {
                    mO(); 
                    mN(); 
                    mE(); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:576:18: Q U O R U M
                    {
                    mQ(); 
                    mU(); 
                    mO(); 
                    mR(); 
                    mU(); 
                    mM(); 

                    }
                    break;
                case 3 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:577:18: A L L
                    {
                    mA(); 
                    mL(); 
                    mL(); 

                    }
                    break;
                case 4 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:578:18: A N Y
                    {
                    mA(); 
                    mN(); 
                    mY(); 

                    }
                    break;
                case 5 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:579:18: L O C A L '_' Q U O R U M
                    {
                    mL(); 
                    mO(); 
                    mC(); 
                    mA(); 
                    mL(); 
                    match('_'); 
                    mQ(); 
                    mU(); 
                    mO(); 
                    mR(); 
                    mU(); 
                    mM(); 

                    }
                    break;
                case 6 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:580:18: E A C H '_' Q U O R U M
                    {
                    mE(); 
                    mA(); 
                    mC(); 
                    mH(); 
                    match('_'); 
                    mQ(); 
                    mU(); 
                    mO(); 
                    mR(); 
                    mU(); 
                    mM(); 

                    }
                    break;
                case 7 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:581:18: T W O
                    {
                    mT(); 
                    mW(); 
                    mO(); 

                    }
                    break;
                case 8 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:582:18: T H R E E
                    {
                    mT(); 
                    mH(); 
                    mR(); 
                    mE(); 
                    mE(); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_LEVEL"

    // $ANTLR start "K_USE"
    public final void mK_USE() throws RecognitionException {
        try {
            int _type = K_USE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:585:6: ( U S E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:585:16: U S E
            {
            mU(); 
            mS(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_USE"

    // $ANTLR start "K_COUNT"
    public final void mK_COUNT() throws RecognitionException {
        try {
            int _type = K_COUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:586:8: ( C O U N T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:586:16: C O U N T
            {
            mC(); 
            mO(); 
            mU(); 
            mN(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_COUNT"

    // $ANTLR start "K_SET"
    public final void mK_SET() throws RecognitionException {
        try {
            int _type = K_SET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:587:6: ( S E T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:587:16: S E T
            {
            mS(); 
            mE(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_SET"

    // $ANTLR start "K_BEGIN"
    public final void mK_BEGIN() throws RecognitionException {
        try {
            int _type = K_BEGIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:588:8: ( B E G I N )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:588:16: B E G I N
            {
            mB(); 
            mE(); 
            mG(); 
            mI(); 
            mN(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_BEGIN"

    // $ANTLR start "K_APPLY"
    public final void mK_APPLY() throws RecognitionException {
        try {
            int _type = K_APPLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:589:8: ( A P P L Y )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:589:16: A P P L Y
            {
            mA(); 
            mP(); 
            mP(); 
            mL(); 
            mY(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_APPLY"

    // $ANTLR start "K_BATCH"
    public final void mK_BATCH() throws RecognitionException {
        try {
            int _type = K_BATCH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:590:8: ( B A T C H )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:590:16: B A T C H
            {
            mB(); 
            mA(); 
            mT(); 
            mC(); 
            mH(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_BATCH"

    // $ANTLR start "K_TRUNCATE"
    public final void mK_TRUNCATE() throws RecognitionException {
        try {
            int _type = K_TRUNCATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:591:11: ( T R U N C A T E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:591:16: T R U N C A T E
            {
            mT(); 
            mR(); 
            mU(); 
            mN(); 
            mC(); 
            mA(); 
            mT(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_TRUNCATE"

    // $ANTLR start "K_DELETE"
    public final void mK_DELETE() throws RecognitionException {
        try {
            int _type = K_DELETE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:592:9: ( D E L E T E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:592:16: D E L E T E
            {
            mD(); 
            mE(); 
            mL(); 
            mE(); 
            mT(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_DELETE"

    // $ANTLR start "K_IN"
    public final void mK_IN() throws RecognitionException {
        try {
            int _type = K_IN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:593:5: ( I N )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:593:16: I N
            {
            mI(); 
            mN(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_IN"

    // $ANTLR start "K_CREATE"
    public final void mK_CREATE() throws RecognitionException {
        try {
            int _type = K_CREATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:594:9: ( C R E A T E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:594:16: C R E A T E
            {
            mC(); 
            mR(); 
            mE(); 
            mA(); 
            mT(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_CREATE"

    // $ANTLR start "K_KEYSPACE"
    public final void mK_KEYSPACE() throws RecognitionException {
        try {
            int _type = K_KEYSPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:595:11: ( ( K E Y S P A C E | S C H E M A ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:595:16: ( K E Y S P A C E | S C H E M A )
            {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:595:16: ( K E Y S P A C E | S C H E M A )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='K'||LA2_0=='k') ) {
                alt2=1;
            }
            else if ( (LA2_0=='S'||LA2_0=='s') ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:595:18: K E Y S P A C E
                    {
                    mK(); 
                    mE(); 
                    mY(); 
                    mS(); 
                    mP(); 
                    mA(); 
                    mC(); 
                    mE(); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:596:20: S C H E M A
                    {
                    mS(); 
                    mC(); 
                    mH(); 
                    mE(); 
                    mM(); 
                    mA(); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_KEYSPACE"

    // $ANTLR start "K_COLUMNFAMILY"
    public final void mK_COLUMNFAMILY() throws RecognitionException {
        try {
            int _type = K_COLUMNFAMILY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:597:15: ( ( C O L U M N F A M I L Y | T A B L E ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:597:16: ( C O L U M N F A M I L Y | T A B L E )
            {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:597:16: ( C O L U M N F A M I L Y | T A B L E )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='C'||LA3_0=='c') ) {
                alt3=1;
            }
            else if ( (LA3_0=='T'||LA3_0=='t') ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:597:18: C O L U M N F A M I L Y
                    {
                    mC(); 
                    mO(); 
                    mL(); 
                    mU(); 
                    mM(); 
                    mN(); 
                    mF(); 
                    mA(); 
                    mM(); 
                    mI(); 
                    mL(); 
                    mY(); 

                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:598:20: T A B L E
                    {
                    mT(); 
                    mA(); 
                    mB(); 
                    mL(); 
                    mE(); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_COLUMNFAMILY"

    // $ANTLR start "K_INDEX"
    public final void mK_INDEX() throws RecognitionException {
        try {
            int _type = K_INDEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:599:8: ( I N D E X )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:599:16: I N D E X
            {
            mI(); 
            mN(); 
            mD(); 
            mE(); 
            mX(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_INDEX"

    // $ANTLR start "K_ON"
    public final void mK_ON() throws RecognitionException {
        try {
            int _type = K_ON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:600:5: ( O N )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:600:16: O N
            {
            mO(); 
            mN(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_ON"

    // $ANTLR start "K_DROP"
    public final void mK_DROP() throws RecognitionException {
        try {
            int _type = K_DROP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:601:7: ( D R O P )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:601:16: D R O P
            {
            mD(); 
            mR(); 
            mO(); 
            mP(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_DROP"

    // $ANTLR start "K_PRIMARY"
    public final void mK_PRIMARY() throws RecognitionException {
        try {
            int _type = K_PRIMARY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:602:10: ( P R I M A R Y )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:602:16: P R I M A R Y
            {
            mP(); 
            mR(); 
            mI(); 
            mM(); 
            mA(); 
            mR(); 
            mY(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_PRIMARY"

    // $ANTLR start "K_INTO"
    public final void mK_INTO() throws RecognitionException {
        try {
            int _type = K_INTO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:603:7: ( I N T O )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:603:16: I N T O
            {
            mI(); 
            mN(); 
            mT(); 
            mO(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_INTO"

    // $ANTLR start "K_VALUES"
    public final void mK_VALUES() throws RecognitionException {
        try {
            int _type = K_VALUES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:604:9: ( V A L U E S )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:604:16: V A L U E S
            {
            mV(); 
            mA(); 
            mL(); 
            mU(); 
            mE(); 
            mS(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_VALUES"

    // $ANTLR start "K_TIMESTAMP"
    public final void mK_TIMESTAMP() throws RecognitionException {
        try {
            int _type = K_TIMESTAMP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:605:12: ( T I M E S T A M P )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:605:16: T I M E S T A M P
            {
            mT(); 
            mI(); 
            mM(); 
            mE(); 
            mS(); 
            mT(); 
            mA(); 
            mM(); 
            mP(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_TIMESTAMP"

    // $ANTLR start "K_TTL"
    public final void mK_TTL() throws RecognitionException {
        try {
            int _type = K_TTL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:606:6: ( T T L )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:606:16: T T L
            {
            mT(); 
            mT(); 
            mL(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_TTL"

    // $ANTLR start "K_ALTER"
    public final void mK_ALTER() throws RecognitionException {
        try {
            int _type = K_ALTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:607:8: ( A L T E R )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:607:16: A L T E R
            {
            mA(); 
            mL(); 
            mT(); 
            mE(); 
            mR(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_ALTER"

    // $ANTLR start "K_ADD"
    public final void mK_ADD() throws RecognitionException {
        try {
            int _type = K_ADD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:608:6: ( A D D )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:608:16: A D D
            {
            mA(); 
            mD(); 
            mD(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_ADD"

    // $ANTLR start "K_TYPE"
    public final void mK_TYPE() throws RecognitionException {
        try {
            int _type = K_TYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:609:7: ( T Y P E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:609:16: T Y P E
            {
            mT(); 
            mY(); 
            mP(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_TYPE"

    // $ANTLR start "K_COMPACT"
    public final void mK_COMPACT() throws RecognitionException {
        try {
            int _type = K_COMPACT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:610:10: ( C O M P A C T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:610:16: C O M P A C T
            {
            mC(); 
            mO(); 
            mM(); 
            mP(); 
            mA(); 
            mC(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_COMPACT"

    // $ANTLR start "K_STORAGE"
    public final void mK_STORAGE() throws RecognitionException {
        try {
            int _type = K_STORAGE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:611:10: ( S T O R A G E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:611:16: S T O R A G E
            {
            mS(); 
            mT(); 
            mO(); 
            mR(); 
            mA(); 
            mG(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_STORAGE"

    // $ANTLR start "K_ORDER"
    public final void mK_ORDER() throws RecognitionException {
        try {
            int _type = K_ORDER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:612:8: ( O R D E R )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:612:16: O R D E R
            {
            mO(); 
            mR(); 
            mD(); 
            mE(); 
            mR(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_ORDER"

    // $ANTLR start "K_BY"
    public final void mK_BY() throws RecognitionException {
        try {
            int _type = K_BY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:613:5: ( B Y )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:613:16: B Y
            {
            mB(); 
            mY(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_BY"

    // $ANTLR start "K_ASC"
    public final void mK_ASC() throws RecognitionException {
        try {
            int _type = K_ASC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:614:6: ( A S C )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:614:16: A S C
            {
            mA(); 
            mS(); 
            mC(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_ASC"

    // $ANTLR start "K_DESC"
    public final void mK_DESC() throws RecognitionException {
        try {
            int _type = K_DESC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:615:7: ( D E S C )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:615:16: D E S C
            {
            mD(); 
            mE(); 
            mS(); 
            mC(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_DESC"

    // $ANTLR start "K_CLUSTERING"
    public final void mK_CLUSTERING() throws RecognitionException {
        try {
            int _type = K_CLUSTERING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:616:13: ( C L U S T E R I N G )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:616:16: C L U S T E R I N G
            {
            mC(); 
            mL(); 
            mU(); 
            mS(); 
            mT(); 
            mE(); 
            mR(); 
            mI(); 
            mN(); 
            mG(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_CLUSTERING"

    // $ANTLR start "K_ASCII"
    public final void mK_ASCII() throws RecognitionException {
        try {
            int _type = K_ASCII;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:618:8: ( A S C I I )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:618:16: A S C I I
            {
            mA(); 
            mS(); 
            mC(); 
            mI(); 
            mI(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_ASCII"

    // $ANTLR start "K_BIGINT"
    public final void mK_BIGINT() throws RecognitionException {
        try {
            int _type = K_BIGINT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:619:9: ( B I G I N T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:619:16: B I G I N T
            {
            mB(); 
            mI(); 
            mG(); 
            mI(); 
            mN(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_BIGINT"

    // $ANTLR start "K_BLOB"
    public final void mK_BLOB() throws RecognitionException {
        try {
            int _type = K_BLOB;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:620:7: ( B L O B )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:620:16: B L O B
            {
            mB(); 
            mL(); 
            mO(); 
            mB(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_BLOB"

    // $ANTLR start "K_BOOLEAN"
    public final void mK_BOOLEAN() throws RecognitionException {
        try {
            int _type = K_BOOLEAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:621:10: ( B O O L E A N )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:621:16: B O O L E A N
            {
            mB(); 
            mO(); 
            mO(); 
            mL(); 
            mE(); 
            mA(); 
            mN(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_BOOLEAN"

    // $ANTLR start "K_COUNTER"
    public final void mK_COUNTER() throws RecognitionException {
        try {
            int _type = K_COUNTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:622:10: ( C O U N T E R )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:622:16: C O U N T E R
            {
            mC(); 
            mO(); 
            mU(); 
            mN(); 
            mT(); 
            mE(); 
            mR(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_COUNTER"

    // $ANTLR start "K_DECIMAL"
    public final void mK_DECIMAL() throws RecognitionException {
        try {
            int _type = K_DECIMAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:623:10: ( D E C I M A L )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:623:16: D E C I M A L
            {
            mD(); 
            mE(); 
            mC(); 
            mI(); 
            mM(); 
            mA(); 
            mL(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_DECIMAL"

    // $ANTLR start "K_DOUBLE"
    public final void mK_DOUBLE() throws RecognitionException {
        try {
            int _type = K_DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:624:9: ( D O U B L E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:624:16: D O U B L E
            {
            mD(); 
            mO(); 
            mU(); 
            mB(); 
            mL(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_DOUBLE"

    // $ANTLR start "K_FLOAT"
    public final void mK_FLOAT() throws RecognitionException {
        try {
            int _type = K_FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:625:8: ( F L O A T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:625:16: F L O A T
            {
            mF(); 
            mL(); 
            mO(); 
            mA(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_FLOAT"

    // $ANTLR start "K_INT"
    public final void mK_INT() throws RecognitionException {
        try {
            int _type = K_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:626:6: ( I N T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:626:16: I N T
            {
            mI(); 
            mN(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_INT"

    // $ANTLR start "K_TEXT"
    public final void mK_TEXT() throws RecognitionException {
        try {
            int _type = K_TEXT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:627:7: ( T E X T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:627:16: T E X T
            {
            mT(); 
            mE(); 
            mX(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_TEXT"

    // $ANTLR start "K_UUID"
    public final void mK_UUID() throws RecognitionException {
        try {
            int _type = K_UUID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:628:7: ( U U I D )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:628:16: U U I D
            {
            mU(); 
            mU(); 
            mI(); 
            mD(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_UUID"

    // $ANTLR start "K_VARCHAR"
    public final void mK_VARCHAR() throws RecognitionException {
        try {
            int _type = K_VARCHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:629:10: ( V A R C H A R )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:629:16: V A R C H A R
            {
            mV(); 
            mA(); 
            mR(); 
            mC(); 
            mH(); 
            mA(); 
            mR(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_VARCHAR"

    // $ANTLR start "K_VARINT"
    public final void mK_VARINT() throws RecognitionException {
        try {
            int _type = K_VARINT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:630:9: ( V A R I N T )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:630:16: V A R I N T
            {
            mV(); 
            mA(); 
            mR(); 
            mI(); 
            mN(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_VARINT"

    // $ANTLR start "K_TIMEUUID"
    public final void mK_TIMEUUID() throws RecognitionException {
        try {
            int _type = K_TIMEUUID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:631:11: ( T I M E U U I D )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:631:16: T I M E U U I D
            {
            mT(); 
            mI(); 
            mM(); 
            mE(); 
            mU(); 
            mU(); 
            mI(); 
            mD(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_TIMEUUID"

    // $ANTLR start "K_TOKEN"
    public final void mK_TOKEN() throws RecognitionException {
        try {
            int _type = K_TOKEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:632:8: ( T O K E N )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:632:16: T O K E N
            {
            mT(); 
            mO(); 
            mK(); 
            mE(); 
            mN(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_TOKEN"

    // $ANTLR start "K_WRITETIME"
    public final void mK_WRITETIME() throws RecognitionException {
        try {
            int _type = K_WRITETIME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:633:12: ( W R I T E T I M E )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:633:16: W R I T E T I M E
            {
            mW(); 
            mR(); 
            mI(); 
            mT(); 
            mE(); 
            mT(); 
            mI(); 
            mM(); 
            mE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "K_WRITETIME"

    // $ANTLR start "A"
    public final void mA() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:636:11: ( ( 'a' | 'A' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:636:13: ( 'a' | 'A' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "A"

    // $ANTLR start "B"
    public final void mB() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:637:11: ( ( 'b' | 'B' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:637:13: ( 'b' | 'B' )
            {
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "B"

    // $ANTLR start "C"
    public final void mC() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:638:11: ( ( 'c' | 'C' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:638:13: ( 'c' | 'C' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "C"

    // $ANTLR start "D"
    public final void mD() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:639:11: ( ( 'd' | 'D' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:639:13: ( 'd' | 'D' )
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "D"

    // $ANTLR start "E"
    public final void mE() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:640:11: ( ( 'e' | 'E' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:640:13: ( 'e' | 'E' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "E"

    // $ANTLR start "F"
    public final void mF() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:641:11: ( ( 'f' | 'F' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:641:13: ( 'f' | 'F' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "F"

    // $ANTLR start "G"
    public final void mG() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:642:11: ( ( 'g' | 'G' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:642:13: ( 'g' | 'G' )
            {
            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "G"

    // $ANTLR start "H"
    public final void mH() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:643:11: ( ( 'h' | 'H' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:643:13: ( 'h' | 'H' )
            {
            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "H"

    // $ANTLR start "I"
    public final void mI() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:644:11: ( ( 'i' | 'I' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:644:13: ( 'i' | 'I' )
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "I"

    // $ANTLR start "J"
    public final void mJ() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:645:11: ( ( 'j' | 'J' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:645:13: ( 'j' | 'J' )
            {
            if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "J"

    // $ANTLR start "K"
    public final void mK() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:646:11: ( ( 'k' | 'K' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:646:13: ( 'k' | 'K' )
            {
            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "K"

    // $ANTLR start "L"
    public final void mL() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:647:11: ( ( 'l' | 'L' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:647:13: ( 'l' | 'L' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "L"

    // $ANTLR start "M"
    public final void mM() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:648:11: ( ( 'm' | 'M' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:648:13: ( 'm' | 'M' )
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "M"

    // $ANTLR start "N"
    public final void mN() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:649:11: ( ( 'n' | 'N' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:649:13: ( 'n' | 'N' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "N"

    // $ANTLR start "O"
    public final void mO() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:650:11: ( ( 'o' | 'O' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:650:13: ( 'o' | 'O' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "O"

    // $ANTLR start "P"
    public final void mP() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:651:11: ( ( 'p' | 'P' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:651:13: ( 'p' | 'P' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "P"

    // $ANTLR start "Q"
    public final void mQ() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:652:11: ( ( 'q' | 'Q' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:652:13: ( 'q' | 'Q' )
            {
            if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Q"

    // $ANTLR start "R"
    public final void mR() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:653:11: ( ( 'r' | 'R' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:653:13: ( 'r' | 'R' )
            {
            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "R"

    // $ANTLR start "S"
    public final void mS() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:654:11: ( ( 's' | 'S' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:654:13: ( 's' | 'S' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "S"

    // $ANTLR start "T"
    public final void mT() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:655:11: ( ( 't' | 'T' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:655:13: ( 't' | 'T' )
            {
            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "T"

    // $ANTLR start "U"
    public final void mU() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:656:11: ( ( 'u' | 'U' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:656:13: ( 'u' | 'U' )
            {
            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "U"

    // $ANTLR start "V"
    public final void mV() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:657:11: ( ( 'v' | 'V' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:657:13: ( 'v' | 'V' )
            {
            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "V"

    // $ANTLR start "W"
    public final void mW() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:658:11: ( ( 'w' | 'W' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:658:13: ( 'w' | 'W' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "W"

    // $ANTLR start "X"
    public final void mX() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:659:11: ( ( 'x' | 'X' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:659:13: ( 'x' | 'X' )
            {
            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "X"

    // $ANTLR start "Y"
    public final void mY() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:660:11: ( ( 'y' | 'Y' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:660:13: ( 'y' | 'Y' )
            {
            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Y"

    // $ANTLR start "Z"
    public final void mZ() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:661:11: ( ( 'z' | 'Z' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:661:13: ( 'z' | 'Z' )
            {
            if ( input.LA(1)=='Z'||input.LA(1)=='z' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Z"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int c;

             StringBuilder b = new StringBuilder(); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:666:5: ( '\\'' (c=~ ( '\\'' ) | '\\'' '\\'' )* '\\'' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:666:7: '\\'' (c=~ ( '\\'' ) | '\\'' '\\'' )* '\\''
            {
            match('\''); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:666:12: (c=~ ( '\\'' ) | '\\'' '\\'' )*
            loop4:
            do {
                int alt4=3;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='\'') ) {
                    int LA4_1 = input.LA(2);

                    if ( (LA4_1=='\'') ) {
                        alt4=2;
                    }


                }
                else if ( ((LA4_0>='\u0000' && LA4_0<='&')||(LA4_0>='(' && LA4_0<='\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:666:13: c=~ ( '\\'' )
            	    {
            	    c= input.LA(1);
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}

            	     b.appendCodePoint(c);

            	    }
            	    break;
            	case 2 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:666:50: '\\'' '\\''
            	    {
            	    match('\''); 
            	    match('\''); 
            	     b.appendCodePoint('\''); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
             setText(b.toString());     }
        finally {
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "QUOTED_NAME"
    public final void mQUOTED_NAME() throws RecognitionException {
        try {
            int _type = QUOTED_NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int c;

             StringBuilder b = new StringBuilder(); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:672:5: ( '\\\"' (c=~ ( '\\\"' ) | '\\\"' '\\\"' )* '\\\"' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:672:7: '\\\"' (c=~ ( '\\\"' ) | '\\\"' '\\\"' )* '\\\"'
            {
            match('\"'); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:672:12: (c=~ ( '\\\"' ) | '\\\"' '\\\"' )*
            loop5:
            do {
                int alt5=3;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\"') ) {
                    int LA5_1 = input.LA(2);

                    if ( (LA5_1=='\"') ) {
                        alt5=2;
                    }


                }
                else if ( ((LA5_0>='\u0000' && LA5_0<='!')||(LA5_0>='#' && LA5_0<='\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:672:13: c=~ ( '\\\"' )
            	    {
            	    c= input.LA(1);
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}

            	     b.appendCodePoint(c); 

            	    }
            	    break;
            	case 2 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:672:51: '\\\"' '\\\"'
            	    {
            	    match('\"'); 
            	    match('\"'); 
            	     b.appendCodePoint('\"'); 

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
             setText(b.toString());     }
        finally {
        }
    }
    // $ANTLR end "QUOTED_NAME"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:676:5: ( '0' .. '9' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:676:7: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:680:5: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:680:7: ( 'A' .. 'Z' | 'a' .. 'z' )
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "HEX"
    public final void mHEX() throws RecognitionException {
        try {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:684:5: ( ( 'A' .. 'F' | 'a' .. 'f' | '0' .. '9' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:684:7: ( 'A' .. 'F' | 'a' .. 'f' | '0' .. '9' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "HEX"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:688:5: ( ( '-' )? ( DIGIT )+ )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:688:7: ( '-' )? ( DIGIT )+
            {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:688:7: ( '-' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='-') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:688:7: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:688:12: ( DIGIT )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:688:12: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "QMARK"
    public final void mQMARK() throws RecognitionException {
        try {
            int _type = QMARK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:692:5: ( '?' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:692:7: '?'
            {
            match('?'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QMARK"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:700:5: ( INTEGER '.' ( DIGIT )* )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:700:7: INTEGER '.' ( DIGIT )*
            {
            mINTEGER(); 
            match('.'); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:700:19: ( DIGIT )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:700:19: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "IDENT"
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:704:5: ( LETTER ( LETTER | DIGIT | '_' )* )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:704:7: LETTER ( LETTER | DIGIT | '_' )*
            {
            mLETTER(); 
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:704:14: ( LETTER | DIGIT | '_' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='0' && LA9_0<='9')||(LA9_0>='A' && LA9_0<='Z')||LA9_0=='_'||(LA9_0>='a' && LA9_0<='z')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENT"

    // $ANTLR start "UUID"
    public final void mUUID() throws RecognitionException {
        try {
            int _type = UUID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:708:5: ( HEX HEX HEX HEX HEX HEX HEX HEX '-' HEX HEX HEX HEX '-' HEX HEX HEX HEX '-' HEX HEX HEX HEX '-' HEX HEX HEX HEX HEX HEX HEX HEX HEX HEX HEX HEX )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:708:7: HEX HEX HEX HEX HEX HEX HEX HEX '-' HEX HEX HEX HEX '-' HEX HEX HEX HEX '-' HEX HEX HEX HEX '-' HEX HEX HEX HEX HEX HEX HEX HEX HEX HEX HEX HEX
            {
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            match('-'); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            match('-'); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            match('-'); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            match('-'); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 
            mHEX(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UUID"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:716:5: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:716:7: ( ' ' | '\\t' | '\\n' | '\\r' )+
            {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:716:7: ( ' ' | '\\t' | '\\n' | '\\r' )+
            int cnt10=0;
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0>='\t' && LA10_0<='\n')||LA10_0=='\r'||LA10_0==' ') ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt10 >= 1 ) break loop10;
                        EarlyExitException eee =
                            new EarlyExitException(10, input);
                        throw eee;
                }
                cnt10++;
            } while (true);

             _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:720:5: ( ( '--' | '//' ) ( . )* ( '\\n' | '\\r' ) )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:720:7: ( '--' | '//' ) ( . )* ( '\\n' | '\\r' )
            {
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:720:7: ( '--' | '//' )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='-') ) {
                alt11=1;
            }
            else if ( (LA11_0=='/') ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:720:8: '--'
                    {
                    match("--"); 


                    }
                    break;
                case 2 :
                    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:720:15: '//'
                    {
                    match("//"); 


                    }
                    break;

            }

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:720:21: ( . )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0=='\n'||LA12_0=='\r') ) {
                    alt12=2;
                }
                else if ( ((LA12_0>='\u0000' && LA12_0<='\t')||(LA12_0>='\u000B' && LA12_0<='\f')||(LA12_0>='\u000E' && LA12_0<='\uFFFF')) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:720:21: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

             _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "MULTILINE_COMMENT"
    public final void mMULTILINE_COMMENT() throws RecognitionException {
        try {
            int _type = MULTILINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:724:5: ( '/*' ( . )* '*/' )
            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:724:7: '/*' ( . )* '*/'
            {
            match("/*"); 

            // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:724:12: ( . )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0=='*') ) {
                    int LA13_1 = input.LA(2);

                    if ( (LA13_1=='/') ) {
                        alt13=2;
                    }
                    else if ( ((LA13_1>='\u0000' && LA13_1<='.')||(LA13_1>='0' && LA13_1<='\uFFFF')) ) {
                        alt13=1;
                    }


                }
                else if ( ((LA13_0>='\u0000' && LA13_0<=')')||(LA13_0>='+' && LA13_0<='\uFFFF')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:724:12: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

            match("*/"); 

             _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULTILINE_COMMENT"

    public void mTokens() throws RecognitionException {
        // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:8: ( T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | K_SELECT | K_FROM | K_WHERE | K_AND | K_KEY | K_INSERT | K_UPDATE | K_WITH | K_LIMIT | K_USING | K_CONSISTENCY | K_LEVEL | K_USE | K_COUNT | K_SET | K_BEGIN | K_APPLY | K_BATCH | K_TRUNCATE | K_DELETE | K_IN | K_CREATE | K_KEYSPACE | K_COLUMNFAMILY | K_INDEX | K_ON | K_DROP | K_PRIMARY | K_INTO | K_VALUES | K_TIMESTAMP | K_TTL | K_ALTER | K_ADD | K_TYPE | K_COMPACT | K_STORAGE | K_ORDER | K_BY | K_ASC | K_DESC | K_CLUSTERING | K_ASCII | K_BIGINT | K_BLOB | K_BOOLEAN | K_COUNTER | K_DECIMAL | K_DOUBLE | K_FLOAT | K_INT | K_TEXT | K_UUID | K_VARCHAR | K_VARINT | K_TIMEUUID | K_TOKEN | K_WRITETIME | STRING_LITERAL | QUOTED_NAME | INTEGER | QMARK | FLOAT | IDENT | UUID | WS | COMMENT | MULTILINE_COMMENT )
        int alt14=82;
        alt14 = dfa14.predict(input);
        switch (alt14) {
            case 1 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:10: T__101
                {
                mT__101(); 

                }
                break;
            case 2 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:17: T__102
                {
                mT__102(); 

                }
                break;
            case 3 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:24: T__103
                {
                mT__103(); 

                }
                break;
            case 4 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:31: T__104
                {
                mT__104(); 

                }
                break;
            case 5 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:38: T__105
                {
                mT__105(); 

                }
                break;
            case 6 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:45: T__106
                {
                mT__106(); 

                }
                break;
            case 7 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:52: T__107
                {
                mT__107(); 

                }
                break;
            case 8 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:59: T__108
                {
                mT__108(); 

                }
                break;
            case 9 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:66: T__109
                {
                mT__109(); 

                }
                break;
            case 10 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:73: T__110
                {
                mT__110(); 

                }
                break;
            case 11 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:80: T__111
                {
                mT__111(); 

                }
                break;
            case 12 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:87: T__112
                {
                mT__112(); 

                }
                break;
            case 13 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:94: T__113
                {
                mT__113(); 

                }
                break;
            case 14 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:101: T__114
                {
                mT__114(); 

                }
                break;
            case 15 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:108: K_SELECT
                {
                mK_SELECT(); 

                }
                break;
            case 16 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:117: K_FROM
                {
                mK_FROM(); 

                }
                break;
            case 17 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:124: K_WHERE
                {
                mK_WHERE(); 

                }
                break;
            case 18 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:132: K_AND
                {
                mK_AND(); 

                }
                break;
            case 19 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:138: K_KEY
                {
                mK_KEY(); 

                }
                break;
            case 20 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:144: K_INSERT
                {
                mK_INSERT(); 

                }
                break;
            case 21 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:153: K_UPDATE
                {
                mK_UPDATE(); 

                }
                break;
            case 22 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:162: K_WITH
                {
                mK_WITH(); 

                }
                break;
            case 23 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:169: K_LIMIT
                {
                mK_LIMIT(); 

                }
                break;
            case 24 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:177: K_USING
                {
                mK_USING(); 

                }
                break;
            case 25 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:185: K_CONSISTENCY
                {
                mK_CONSISTENCY(); 

                }
                break;
            case 26 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:199: K_LEVEL
                {
                mK_LEVEL(); 

                }
                break;
            case 27 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:207: K_USE
                {
                mK_USE(); 

                }
                break;
            case 28 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:213: K_COUNT
                {
                mK_COUNT(); 

                }
                break;
            case 29 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:221: K_SET
                {
                mK_SET(); 

                }
                break;
            case 30 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:227: K_BEGIN
                {
                mK_BEGIN(); 

                }
                break;
            case 31 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:235: K_APPLY
                {
                mK_APPLY(); 

                }
                break;
            case 32 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:243: K_BATCH
                {
                mK_BATCH(); 

                }
                break;
            case 33 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:251: K_TRUNCATE
                {
                mK_TRUNCATE(); 

                }
                break;
            case 34 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:262: K_DELETE
                {
                mK_DELETE(); 

                }
                break;
            case 35 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:271: K_IN
                {
                mK_IN(); 

                }
                break;
            case 36 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:276: K_CREATE
                {
                mK_CREATE(); 

                }
                break;
            case 37 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:285: K_KEYSPACE
                {
                mK_KEYSPACE(); 

                }
                break;
            case 38 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:296: K_COLUMNFAMILY
                {
                mK_COLUMNFAMILY(); 

                }
                break;
            case 39 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:311: K_INDEX
                {
                mK_INDEX(); 

                }
                break;
            case 40 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:319: K_ON
                {
                mK_ON(); 

                }
                break;
            case 41 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:324: K_DROP
                {
                mK_DROP(); 

                }
                break;
            case 42 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:331: K_PRIMARY
                {
                mK_PRIMARY(); 

                }
                break;
            case 43 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:341: K_INTO
                {
                mK_INTO(); 

                }
                break;
            case 44 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:348: K_VALUES
                {
                mK_VALUES(); 

                }
                break;
            case 45 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:357: K_TIMESTAMP
                {
                mK_TIMESTAMP(); 

                }
                break;
            case 46 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:369: K_TTL
                {
                mK_TTL(); 

                }
                break;
            case 47 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:375: K_ALTER
                {
                mK_ALTER(); 

                }
                break;
            case 48 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:383: K_ADD
                {
                mK_ADD(); 

                }
                break;
            case 49 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:389: K_TYPE
                {
                mK_TYPE(); 

                }
                break;
            case 50 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:396: K_COMPACT
                {
                mK_COMPACT(); 

                }
                break;
            case 51 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:406: K_STORAGE
                {
                mK_STORAGE(); 

                }
                break;
            case 52 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:416: K_ORDER
                {
                mK_ORDER(); 

                }
                break;
            case 53 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:424: K_BY
                {
                mK_BY(); 

                }
                break;
            case 54 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:429: K_ASC
                {
                mK_ASC(); 

                }
                break;
            case 55 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:435: K_DESC
                {
                mK_DESC(); 

                }
                break;
            case 56 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:442: K_CLUSTERING
                {
                mK_CLUSTERING(); 

                }
                break;
            case 57 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:455: K_ASCII
                {
                mK_ASCII(); 

                }
                break;
            case 58 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:463: K_BIGINT
                {
                mK_BIGINT(); 

                }
                break;
            case 59 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:472: K_BLOB
                {
                mK_BLOB(); 

                }
                break;
            case 60 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:479: K_BOOLEAN
                {
                mK_BOOLEAN(); 

                }
                break;
            case 61 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:489: K_COUNTER
                {
                mK_COUNTER(); 

                }
                break;
            case 62 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:499: K_DECIMAL
                {
                mK_DECIMAL(); 

                }
                break;
            case 63 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:509: K_DOUBLE
                {
                mK_DOUBLE(); 

                }
                break;
            case 64 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:518: K_FLOAT
                {
                mK_FLOAT(); 

                }
                break;
            case 65 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:526: K_INT
                {
                mK_INT(); 

                }
                break;
            case 66 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:532: K_TEXT
                {
                mK_TEXT(); 

                }
                break;
            case 67 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:539: K_UUID
                {
                mK_UUID(); 

                }
                break;
            case 68 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:546: K_VARCHAR
                {
                mK_VARCHAR(); 

                }
                break;
            case 69 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:556: K_VARINT
                {
                mK_VARINT(); 

                }
                break;
            case 70 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:565: K_TIMEUUID
                {
                mK_TIMEUUID(); 

                }
                break;
            case 71 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:576: K_TOKEN
                {
                mK_TOKEN(); 

                }
                break;
            case 72 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:584: K_WRITETIME
                {
                mK_WRITETIME(); 

                }
                break;
            case 73 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:596: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 74 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:611: QUOTED_NAME
                {
                mQUOTED_NAME(); 

                }
                break;
            case 75 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:623: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 76 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:631: QMARK
                {
                mQMARK(); 

                }
                break;
            case 77 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:637: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 78 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:643: IDENT
                {
                mIDENT(); 

                }
                break;
            case 79 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:649: UUID
                {
                mUUID(); 

                }
                break;
            case 80 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:654: WS
                {
                mWS(); 

                }
                break;
            case 81 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:657: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 82 :
                // /home/bhathiya/carbon/platform/branches/4.2.0/dependencies/cassandra/1.1.3-wso2v5/src/java/org/apache/cassandra/cql3/Cql.g:1:665: MULTILINE_COMMENT
                {
                mMULTILINE_COMMENT(); 

                }
                break;

        }

    }


    protected DFA1 dfa1 = new DFA1(this);
    protected DFA14 dfa14 = new DFA14(this);
    static final String DFA1_eotS =
        "\13\uffff";
    static final String DFA1_eofS =
        "\13\uffff";
    static final String DFA1_minS =
        "\1\101\2\uffff\1\114\2\uffff\1\110\4\uffff";
    static final String DFA1_maxS =
        "\1\164\2\uffff\1\156\2\uffff\1\167\4\uffff";
    static final String DFA1_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\5\1\6\1\uffff\1\3\1\4\1\7\1\10";
    static final String DFA1_specialS =
        "\13\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\3\3\uffff\1\5\6\uffff\1\4\2\uffff\1\1\1\uffff\1\2\2\uffff"+
            "\1\6\14\uffff\1\3\3\uffff\1\5\6\uffff\1\4\2\uffff\1\1\1\uffff"+
            "\1\2\2\uffff\1\6",
            "",
            "",
            "\1\7\1\uffff\1\10\35\uffff\1\7\1\uffff\1\10",
            "",
            "",
            "\1\12\16\uffff\1\11\20\uffff\1\12\16\uffff\1\11",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "575:16: ( O N E | Q U O R U M | A L L | A N Y | L O C A L '_' Q U O R U M | E A C H '_' Q U O R U M | T W O | T H R E E )";
        }
    }
    static final String DFA14_eotS =
        "\11\uffff\1\46\1\uffff\1\51\1\53\21\42\2\uffff\1\134\6\uffff\1\134"+
        "\4\uffff\17\42\1\163\10\42\1\u0083\16\42\1\u0093\10\42\1\uffff\1"+
        "\134\3\uffff\1\u00a0\11\42\1\u00aa\1\u00ac\1\42\1\u00ae\1\42\1\u00ae"+
        "\1\u00b0\1\u00b1\1\uffff\1\u00b3\4\42\1\u00b9\11\42\1\uffff\1\u00ae"+
        "\4\42\1\u00ae\3\42\1\u00ca\5\42\1\uffff\13\42\1\134\1\uffff\5\42"+
        "\1\u00e2\2\42\1\u00e5\1\uffff\1\42\1\uffff\1\42\1\uffff\1\42\2\uffff"+
        "\1\42\1\uffff\1\u00ea\4\42\1\uffff\1\u00ef\14\42\1\u00fd\1\42\1"+
        "\u00ff\1\uffff\4\42\1\u0104\3\42\1\u0108\1\u0109\7\42\1\134\3\42"+
        "\1\u0115\1\42\1\uffff\1\42\1\u0118\1\uffff\1\u0119\1\u011a\1\u011b"+
        "\1\42\1\uffff\1\42\1\u011e\1\42\1\u0120\1\uffff\1\u0121\1\42\1\u0123"+
        "\5\42\1\u012a\4\42\1\uffff\1\42\1\uffff\1\u0130\1\u00ae\1\u0131"+
        "\1\42\1\uffff\1\u0133\1\42\1\u0135\2\uffff\7\42\1\134\1\u013e\1"+
        "\42\1\u0140\1\uffff\2\42\4\uffff\1\42\1\u0144\1\uffff\1\u0145\2"+
        "\uffff\1\42\1\uffff\5\42\1\u014c\1\uffff\1\u00ae\4\42\2\uffff\1"+
        "\u0151\1\uffff\1\42\1\uffff\1\u0153\1\42\1\u0155\1\42\1\u0157\1"+
        "\u0158\1\42\1\134\1\uffff\1\u015b\1\uffff\3\42\2\uffff\1\42\1\u0160"+
        "\2\42\1\u0163\1\42\1\uffff\4\42\1\uffff\1\u0169\1\uffff\1\u016a"+
        "\1\uffff\1\u016b\2\uffff\1\u016c\1\134\1\uffff\2\42\1\u0140\1\42"+
        "\1\uffff\2\42\1\uffff\3\42\1\u0175\1\u0176\4\uffff\1\134\1\u0177"+
        "\5\42\1\u017d\3\uffff\3\42\1\u0181\1\42\1\uffff\2\42\1\u0185\1\uffff"+
        "\2\u00ae\1\u0131\1\uffff";
    static final String DFA14_eofS =
        "\u0186\uffff";
    static final String DFA14_minS =
        "\1\11\10\uffff\1\55\1\uffff\2\75\1\103\1\60\1\110\1\60\1\105\1\116"+
        "\1\120\1\111\1\60\1\116\1\125\1\60\1\101\2\60\1\122\1\101\2\uffff"+
        "\1\56\3\uffff\1\52\2\uffff\1\56\4\uffff\1\114\1\117\1\110\1\117"+
        "\1\60\1\117\1\111\1\105\1\124\1\103\1\60\1\120\1\114\1\104\1\131"+
        "\1\60\1\104\1\105\1\111\1\115\1\103\1\114\1\125\1\105\1\60\1\104"+
        "\1\117\1\60\1\115\1\117\1\130\1\125\1\120\1\114\1\113\1\122\1\102"+
        "\1\107\1\117\2\60\1\117\1\60\1\117\1\60\1\125\1\111\1\114\1\uffff"+
        "\1\56\3\uffff\1\60\1\105\1\122\1\105\1\101\1\60\1\115\1\124\1\122"+
        "\1\110\2\60\1\114\1\60\1\105\3\60\1\uffff\1\60\2\105\1\101\1\116"+
        "\1\60\1\104\1\111\1\101\1\116\1\125\1\123\1\120\1\123\1\101\1\uffff"+
        "\1\60\1\105\1\122\1\60\1\105\1\60\1\124\1\116\1\105\1\60\2\105\1"+
        "\114\1\111\1\102\1\uffff\1\103\1\114\1\111\1\120\1\103\1\105\1\60"+
        "\1\102\1\115\1\125\1\103\1\56\1\uffff\1\103\1\101\1\115\1\124\2"+
        "\60\2\105\1\60\1\uffff\1\111\1\uffff\1\131\1\uffff\1\122\2\uffff"+
        "\1\120\1\uffff\1\60\1\122\1\130\1\124\1\107\1\uffff\1\60\1\124\1"+
        "\114\1\124\1\115\1\111\1\101\2\124\1\122\1\125\1\137\1\123\1\60"+
        "\1\103\1\60\1\uffff\1\116\2\105\1\116\1\60\1\110\1\105\1\116\2\60"+
        "\1\124\1\115\1\114\1\101\1\105\1\116\1\110\1\56\1\124\1\107\1\101"+
        "\2\60\1\uffff\1\124\1\60\1\uffff\3\60\1\101\1\uffff\1\124\1\60\1"+
        "\105\1\60\1\uffff\1\60\1\137\1\60\1\116\1\123\1\103\2\105\1\60\1"+
        "\115\1\121\1\124\1\125\1\uffff\1\101\1\uffff\3\60\1\124\1\uffff"+
        "\1\60\1\101\1\60\2\uffff\1\105\1\101\1\105\1\122\1\123\1\124\1\101"+
        "\1\56\1\60\1\105\1\60\1\uffff\1\60\1\111\4\uffff\1\103\1\60\1\uffff"+
        "\1\60\2\uffff\1\121\1\uffff\1\122\1\106\2\124\1\122\1\60\1\uffff"+
        "\1\60\1\125\1\101\1\111\1\124\2\uffff\1\60\1\uffff\1\116\1\uffff"+
        "\1\60\1\114\1\60\1\131\2\60\1\122\1\56\1\uffff\1\60\1\uffff\1\60"+
        "\1\115\1\105\2\uffff\1\125\1\60\1\101\1\105\1\60\1\111\1\uffff\1"+
        "\117\1\115\1\104\1\105\1\uffff\1\60\1\uffff\1\60\1\uffff\1\60\2"+
        "\uffff\1\60\1\56\1\uffff\1\55\1\105\1\60\1\117\1\uffff\1\115\1\116"+
        "\1\uffff\1\116\1\122\1\120\2\60\4\uffff\1\55\1\60\1\122\1\111\1"+
        "\103\1\107\1\125\1\60\3\uffff\1\125\1\114\1\131\1\60\1\115\1\uffff"+
        "\1\115\1\131\1\60\1\uffff\3\60\1\uffff";
    static final String DFA14_maxS =
        "\1\172\10\uffff\1\71\1\uffff\2\75\1\164\2\162\1\163\1\145\1\156"+
        "\1\165\1\157\2\162\1\165\1\146\2\171\2\162\1\141\2\uffff\1\146\3"+
        "\uffff\1\57\2\uffff\1\71\4\uffff\1\164\1\157\1\150\1\157\1\146\1"+
        "\157\1\151\1\145\1\164\1\143\1\146\1\160\1\164\2\171\1\172\1\144"+
        "\2\151\1\155\1\143\2\165\1\145\1\172\1\144\1\157\1\146\1\155\1\157"+
        "\1\170\1\165\1\160\1\154\1\153\1\162\1\142\1\147\1\157\1\172\1\164"+
        "\1\157\1\147\1\157\1\163\1\165\1\151\1\162\1\uffff\1\146\3\uffff"+
        "\1\172\1\145\1\162\1\145\1\141\1\146\1\155\1\164\1\162\1\150\2\172"+
        "\1\154\1\172\1\145\3\172\1\uffff\1\172\2\145\1\141\1\156\1\172\1"+
        "\144\1\151\1\141\1\156\1\165\1\163\1\160\1\163\1\141\1\uffff\1\172"+
        "\1\145\1\162\1\150\1\145\1\172\1\164\1\156\1\145\1\172\2\145\1\154"+
        "\1\151\1\142\1\uffff\1\143\1\154\1\151\1\160\1\143\1\145\1\151\1"+
        "\142\1\155\1\165\1\151\1\146\1\uffff\1\143\1\141\1\155\1\164\1\146"+
        "\1\172\2\145\1\172\1\uffff\1\151\1\uffff\1\171\1\uffff\1\162\2\uffff"+
        "\1\160\1\uffff\1\172\1\162\1\170\1\164\1\147\1\uffff\1\172\1\164"+
        "\1\154\1\164\1\155\1\151\1\141\2\164\1\162\1\165\1\137\1\165\1\172"+
        "\1\143\1\172\1\uffff\1\156\2\145\1\156\1\172\1\150\1\145\1\156\2"+
        "\172\1\164\1\155\1\154\1\141\1\145\1\156\1\150\1\146\1\164\1\147"+
        "\1\141\1\172\1\146\1\uffff\1\164\1\172\1\uffff\3\172\1\141\1\uffff"+
        "\1\164\1\172\1\145\1\172\1\uffff\1\172\1\137\1\172\1\156\1\163\1"+
        "\143\2\145\1\172\1\155\1\161\1\164\1\165\1\uffff\1\141\1\uffff\3"+
        "\172\1\164\1\uffff\1\172\1\141\1\172\2\uffff\1\145\1\141\1\145\1"+
        "\162\1\163\1\164\1\141\1\146\1\172\1\145\1\172\1\uffff\1\146\1\151"+
        "\4\uffff\1\143\1\172\1\uffff\1\172\2\uffff\1\161\1\uffff\1\162\1"+
        "\146\2\164\1\162\1\172\1\uffff\1\172\1\165\1\141\1\151\1\164\2\uffff"+
        "\1\172\1\uffff\1\156\1\uffff\1\172\1\154\1\172\1\171\2\172\1\162"+
        "\1\146\1\uffff\1\172\1\uffff\1\146\1\155\1\145\2\uffff\1\165\1\172"+
        "\1\141\1\145\1\172\1\151\1\uffff\1\157\1\155\1\144\1\145\1\uffff"+
        "\1\172\1\uffff\1\172\1\uffff\1\172\2\uffff\1\172\1\146\1\uffff\1"+
        "\55\1\145\1\172\1\157\1\uffff\1\155\1\156\1\uffff\1\156\1\162\1"+
        "\160\2\172\4\uffff\1\71\1\172\1\162\1\151\1\143\1\147\1\165\1\172"+
        "\3\uffff\1\165\1\154\1\171\1\172\1\155\1\uffff\1\155\1\171\1\172"+
        "\1\uffff\3\172\1\uffff";
    static final String DFA14_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\uffff\1\12\23\uffff"+
        "\1\111\1\112\1\uffff\1\114\1\116\1\120\1\uffff\1\121\1\11\1\uffff"+
        "\1\14\1\13\1\15\1\16\60\uffff\1\113\1\uffff\1\117\1\115\1\122\22"+
        "\uffff\1\43\17\uffff\1\50\17\uffff\1\65\14\uffff\1\35\11\uffff\1"+
        "\66\1\uffff\1\60\1\uffff\1\32\1\uffff\1\22\1\23\1\uffff\1\101\5"+
        "\uffff\1\33\20\uffff\1\56\27\uffff\1\20\2\uffff\1\26\4\uffff\1\53"+
        "\4\uffff\1\103\15\uffff\1\102\1\uffff\1\61\4\uffff\1\73\3\uffff"+
        "\1\51\1\67\13\uffff\1\100\2\uffff\1\21\1\71\1\37\1\57\2\uffff\1"+
        "\47\1\uffff\1\30\1\27\1\uffff\1\34\6\uffff\1\64\5\uffff\1\107\1"+
        "\46\1\uffff\1\40\1\uffff\1\36\10\uffff\1\17\1\uffff\1\45\3\uffff"+
        "\1\24\1\25\6\uffff\1\44\4\uffff\1\72\1\uffff\1\42\1\uffff\1\77\1"+
        "\uffff\1\54\1\105\2\uffff\1\63\4\uffff\1\75\2\uffff\1\62\5\uffff"+
        "\1\74\1\76\1\52\1\104\10\uffff\1\106\1\41\1\110\5\uffff\1\55\3\uffff"+
        "\1\70\3\uffff\1\31";
    static final String DFA14_specialS =
        "\u0186\uffff}>";
    static final String[] DFA14_transitionS = {
            "\2\43\2\uffff\1\43\22\uffff\1\43\1\uffff\1\37\4\uffff\1\36\1"+
            "\2\1\3\1\5\1\10\1\4\1\11\1\7\1\44\12\40\1\12\1\1\1\13\1\6\1"+
            "\14\1\41\1\uffff\1\20\1\32\1\25\1\33\1\30\1\16\2\42\1\22\1\42"+
            "\1\21\1\24\2\42\1\26\1\34\1\27\1\42\1\15\1\31\1\23\1\35\1\17"+
            "\3\42\6\uffff\1\20\1\32\1\25\1\33\1\30\1\16\2\42\1\22\1\42\1"+
            "\21\1\24\2\42\1\26\1\34\1\27\1\42\1\15\1\31\1\23\1\35\1\17\3"+
            "\42",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\45\2\uffff\12\47",
            "",
            "\1\50",
            "\1\52",
            "\1\56\1\uffff\1\54\16\uffff\1\55\16\uffff\1\56\1\uffff\1\54"+
            "\16\uffff\1\55",
            "\12\60\7\uffff\6\60\5\uffff\1\57\5\uffff\1\61\16\uffff\6\60"+
            "\5\uffff\1\57\5\uffff\1\61",
            "\1\63\1\64\10\uffff\1\62\25\uffff\1\63\1\64\10\uffff\1\62",
            "\12\60\7\uffff\3\60\1\66\2\60\5\uffff\1\70\1\uffff\1\71\1\uffff"+
            "\1\67\2\uffff\1\65\15\uffff\3\60\1\66\2\60\5\uffff\1\70\1\uffff"+
            "\1\71\1\uffff\1\67\2\uffff\1\65",
            "\1\72\37\uffff\1\72",
            "\1\73\37\uffff\1\73",
            "\1\74\2\uffff\1\75\1\uffff\1\76\32\uffff\1\74\2\uffff\1\75"+
            "\1\uffff\1\76",
            "\1\77\5\uffff\1\100\31\uffff\1\77\5\uffff\1\100",
            "\12\60\7\uffff\6\60\5\uffff\1\102\2\uffff\1\101\2\uffff\1\103"+
            "\16\uffff\6\60\5\uffff\1\102\2\uffff\1\101\2\uffff\1\103",
            "\1\104\3\uffff\1\105\33\uffff\1\104\3\uffff\1\105",
            "\1\106\37\uffff\1\106",
            "\12\60\7\uffff\1\107\5\60\32\uffff\1\107\5\60",
            "\1\120\3\uffff\1\112\2\uffff\1\117\1\110\5\uffff\1\116\2\uffff"+
            "\1\113\1\uffff\1\115\2\uffff\1\111\1\uffff\1\114\7\uffff\1\120"+
            "\3\uffff\1\112\2\uffff\1\117\1\110\5\uffff\1\116\2\uffff\1\113"+
            "\1\uffff\1\115\2\uffff\1\111\1\uffff\1\114",
            "\12\60\7\uffff\1\124\3\60\1\126\1\60\2\uffff\1\121\2\uffff"+
            "\1\122\2\uffff\1\125\11\uffff\1\123\7\uffff\1\124\3\60\1\126"+
            "\1\60\2\uffff\1\121\2\uffff\1\122\2\uffff\1\125\11\uffff\1\123",
            "\12\60\7\uffff\4\60\1\130\1\60\10\uffff\1\131\2\uffff\1\127"+
            "\16\uffff\4\60\1\130\1\60\10\uffff\1\131\2\uffff\1\127",
            "\1\132\37\uffff\1\132",
            "\1\133\37\uffff\1\133",
            "",
            "",
            "\1\137\1\uffff\12\135\7\uffff\6\136\32\uffff\6\136",
            "",
            "",
            "",
            "\1\140\4\uffff\1\45",
            "",
            "",
            "\1\137\1\uffff\12\47",
            "",
            "",
            "",
            "",
            "\1\142\7\uffff\1\141\27\uffff\1\142\7\uffff\1\141",
            "\1\143\37\uffff\1\143",
            "\1\144\37\uffff\1\144",
            "\1\145\37\uffff\1\145",
            "\12\146\7\uffff\6\146\32\uffff\6\146",
            "\1\147\37\uffff\1\147",
            "\1\150\37\uffff\1\150",
            "\1\151\37\uffff\1\151",
            "\1\152\37\uffff\1\152",
            "\1\153\37\uffff\1\153",
            "\12\146\7\uffff\3\146\1\154\2\146\32\uffff\3\146\1\154\2\146",
            "\1\155\37\uffff\1\155",
            "\1\156\7\uffff\1\157\27\uffff\1\156\7\uffff\1\157",
            "\1\161\24\uffff\1\160\12\uffff\1\161\24\uffff\1\160",
            "\1\162\37\uffff\1\162",
            "\12\42\7\uffff\3\42\1\166\16\42\1\165\1\164\6\42\4\uffff\1"+
            "\42\1\uffff\3\42\1\166\16\42\1\165\1\164\6\42",
            "\1\167\37\uffff\1\167",
            "\1\171\3\uffff\1\170\33\uffff\1\171\3\uffff\1\170",
            "\1\172\37\uffff\1\172",
            "\1\173\37\uffff\1\173",
            "\1\174\37\uffff\1\174",
            "\1\176\1\u0080\1\177\6\uffff\1\175\26\uffff\1\176\1\u0080\1"+
            "\177\6\uffff\1\175",
            "\1\u0081\37\uffff\1\u0081",
            "\1\u0082\37\uffff\1\u0082",
            "\12\42\7\uffff\4\42\1\u0084\25\42\4\uffff\1\42\1\uffff\4\42"+
            "\1\u0084\25\42",
            "\1\u0085\37\uffff\1\u0085",
            "\1\u0086\37\uffff\1\u0086",
            "\12\146\7\uffff\2\146\1\u0087\3\146\32\uffff\2\146\1\u0087"+
            "\3\146",
            "\1\u0088\37\uffff\1\u0088",
            "\1\u0089\37\uffff\1\u0089",
            "\1\u008a\37\uffff\1\u008a",
            "\1\u008b\37\uffff\1\u008b",
            "\1\u008c\37\uffff\1\u008c",
            "\1\u008d\37\uffff\1\u008d",
            "\1\u008e\37\uffff\1\u008e",
            "\1\u008f\37\uffff\1\u008f",
            "\1\u0090\37\uffff\1\u0090",
            "\1\u0091\37\uffff\1\u0091",
            "\1\u0092\37\uffff\1\u0092",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\146\7\uffff\6\146\15\uffff\1\u0094\14\uffff\6\146\15\uffff"+
            "\1\u0094",
            "\1\u0095\37\uffff\1\u0095",
            "\12\146\7\uffff\6\146\1\u0096\31\uffff\6\146\1\u0096",
            "\1\u0097\37\uffff\1\u0097",
            "\12\146\7\uffff\2\146\1\u009a\3\146\5\uffff\1\u0099\6\uffff"+
            "\1\u0098\15\uffff\2\146\1\u009a\3\146\5\uffff\1\u0099\6\uffff"+
            "\1\u0098",
            "\1\u009b\37\uffff\1\u009b",
            "\1\u009c\37\uffff\1\u009c",
            "\1\u009d\5\uffff\1\u009e\31\uffff\1\u009d\5\uffff\1\u009e",
            "",
            "\1\137\1\uffff\12\u009f\7\uffff\6\136\32\uffff\6\136",
            "",
            "",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00a1\37\uffff\1\u00a1",
            "\1\u00a2\37\uffff\1\u00a2",
            "\1\u00a3\37\uffff\1\u00a3",
            "\1\u00a4\37\uffff\1\u00a4",
            "\12\u00a5\7\uffff\6\u00a5\32\uffff\6\u00a5",
            "\1\u00a6\37\uffff\1\u00a6",
            "\1\u00a7\37\uffff\1\u00a7",
            "\1\u00a8\37\uffff\1\u00a8",
            "\1\u00a9\37\uffff\1\u00a9",
            "\12\42\7\uffff\10\42\1\u00ab\21\42\4\uffff\1\42\1\uffff\10"+
            "\42\1\u00ab\21\42",
            "\12\u00a5\7\uffff\6\u00a5\24\42\4\uffff\1\42\1\uffff\6\u00a5"+
            "\24\42",
            "\1\u00ad\37\uffff\1\u00ad",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00af\37\uffff\1\u00af",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\22\42\1\u00b2\7\42\4\uffff\1\42\1\uffff\22\42"+
            "\1\u00b2\7\42",
            "",
            "\12\42\7\uffff\16\42\1\u00b4\13\42\4\uffff\1\42\1\uffff\16"+
            "\42\1\u00b4\13\42",
            "\1\u00b5\37\uffff\1\u00b5",
            "\1\u00b6\37\uffff\1\u00b6",
            "\1\u00b7\37\uffff\1\u00b7",
            "\1\u00b8\37\uffff\1\u00b8",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00ba\37\uffff\1\u00ba",
            "\1\u00bb\37\uffff\1\u00bb",
            "\1\u00bc\37\uffff\1\u00bc",
            "\1\u00bd\37\uffff\1\u00bd",
            "\1\u00be\37\uffff\1\u00be",
            "\1\u00bf\37\uffff\1\u00bf",
            "\1\u00c0\37\uffff\1\u00c0",
            "\1\u00c1\37\uffff\1\u00c1",
            "\1\u00c2\37\uffff\1\u00c2",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00c3\37\uffff\1\u00c3",
            "\1\u00c4\37\uffff\1\u00c4",
            "\12\u00a5\7\uffff\6\u00a5\1\uffff\1\u00c5\30\uffff\6\u00a5"+
            "\1\uffff\1\u00c5",
            "\1\u00c6\37\uffff\1\u00c6",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00c7\37\uffff\1\u00c7",
            "\1\u00c8\37\uffff\1\u00c8",
            "\1\u00c9\37\uffff\1\u00c9",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00cb\37\uffff\1\u00cb",
            "\1\u00cc\37\uffff\1\u00cc",
            "\1\u00cd\37\uffff\1\u00cd",
            "\1\u00ce\37\uffff\1\u00ce",
            "\1\u00cf\37\uffff\1\u00cf",
            "",
            "\1\u00d0\37\uffff\1\u00d0",
            "\1\u00d1\37\uffff\1\u00d1",
            "\1\u00d2\37\uffff\1\u00d2",
            "\1\u00d3\37\uffff\1\u00d3",
            "\1\u00d4\37\uffff\1\u00d4",
            "\1\u00d5\37\uffff\1\u00d5",
            "\12\u00a5\7\uffff\6\u00a5\2\uffff\1\u00d6\27\uffff\6\u00a5"+
            "\2\uffff\1\u00d6",
            "\1\u00d7\37\uffff\1\u00d7",
            "\1\u00d8\37\uffff\1\u00d8",
            "\1\u00d9\37\uffff\1\u00d9",
            "\1\u00db\5\uffff\1\u00da\31\uffff\1\u00db\5\uffff\1\u00da",
            "\1\137\1\uffff\12\u00dc\7\uffff\6\136\32\uffff\6\136",
            "",
            "\1\u00dd\37\uffff\1\u00dd",
            "\1\u00de\37\uffff\1\u00de",
            "\1\u00df\37\uffff\1\u00df",
            "\1\u00e0\37\uffff\1\u00e0",
            "\12\u00e1\7\uffff\6\u00e1\32\uffff\6\u00e1",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00e3\37\uffff\1\u00e3",
            "\1\u00e4\37\uffff\1\u00e4",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\1\u00e6\37\uffff\1\u00e6",
            "",
            "\1\u00e7\37\uffff\1\u00e7",
            "",
            "\1\u00e8\37\uffff\1\u00e8",
            "",
            "",
            "\1\u00e9\37\uffff\1\u00e9",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00eb\37\uffff\1\u00eb",
            "\1\u00ec\37\uffff\1\u00ec",
            "\1\u00ed\37\uffff\1\u00ed",
            "\1\u00ee\37\uffff\1\u00ee",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00f0\37\uffff\1\u00f0",
            "\1\u00f1\37\uffff\1\u00f1",
            "\1\u00f2\37\uffff\1\u00f2",
            "\1\u00f3\37\uffff\1\u00f3",
            "\1\u00f4\37\uffff\1\u00f4",
            "\1\u00f5\37\uffff\1\u00f5",
            "\1\u00f6\37\uffff\1\u00f6",
            "\1\u00f7\37\uffff\1\u00f7",
            "\1\u00f8\37\uffff\1\u00f8",
            "\1\u00f9\37\uffff\1\u00f9",
            "\1\u00fa",
            "\1\u00fb\1\uffff\1\u00fc\35\uffff\1\u00fb\1\uffff\1\u00fc",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00fe\37\uffff\1\u00fe",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\1\u0100\37\uffff\1\u0100",
            "\1\u0101\37\uffff\1\u0101",
            "\1\u0102\37\uffff\1\u0102",
            "\1\u0103\37\uffff\1\u0103",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0105\37\uffff\1\u0105",
            "\1\u0106\37\uffff\1\u0106",
            "\1\u0107\37\uffff\1\u0107",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u010a\37\uffff\1\u010a",
            "\1\u010b\37\uffff\1\u010b",
            "\1\u010c\37\uffff\1\u010c",
            "\1\u010d\37\uffff\1\u010d",
            "\1\u010e\37\uffff\1\u010e",
            "\1\u010f\37\uffff\1\u010f",
            "\1\u0110\37\uffff\1\u0110",
            "\1\137\1\uffff\12\u0111\7\uffff\6\136\32\uffff\6\136",
            "\1\u0112\37\uffff\1\u0112",
            "\1\u0113\37\uffff\1\u0113",
            "\1\u0114\37\uffff\1\u0114",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\u0116\7\uffff\6\u0116\32\uffff\6\u0116",
            "",
            "\1\u0117\37\uffff\1\u0117",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u011c\37\uffff\1\u011c",
            "",
            "\1\u011d\37\uffff\1\u011d",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u011f\37\uffff\1\u011f",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0122",
            "\12\42\7\uffff\4\42\1\u0124\25\42\4\uffff\1\42\1\uffff\4\42"+
            "\1\u0124\25\42",
            "\1\u0125\37\uffff\1\u0125",
            "\1\u0126\37\uffff\1\u0126",
            "\1\u0127\37\uffff\1\u0127",
            "\1\u0128\37\uffff\1\u0128",
            "\1\u0129\37\uffff\1\u0129",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u012b\37\uffff\1\u012b",
            "\1\u012c\37\uffff\1\u012c",
            "\1\u012d\37\uffff\1\u012d",
            "\1\u012e\37\uffff\1\u012e",
            "",
            "\1\u012f\37\uffff\1\u012f",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0132\37\uffff\1\u0132",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0134\37\uffff\1\u0134",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "",
            "\1\u0136\37\uffff\1\u0136",
            "\1\u0137\37\uffff\1\u0137",
            "\1\u0138\37\uffff\1\u0138",
            "\1\u0139\37\uffff\1\u0139",
            "\1\u013a\37\uffff\1\u013a",
            "\1\u013b\37\uffff\1\u013b",
            "\1\u013c\37\uffff\1\u013c",
            "\1\137\1\uffff\12\u013d\7\uffff\6\136\32\uffff\6\136",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u013f\37\uffff\1\u013f",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\12\u0141\7\uffff\6\u0141\32\uffff\6\u0141",
            "\1\u0142\37\uffff\1\u0142",
            "",
            "",
            "",
            "",
            "\1\u0143\37\uffff\1\u0143",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "",
            "\1\u0146\37\uffff\1\u0146",
            "",
            "\1\u0147\37\uffff\1\u0147",
            "\1\u0148\37\uffff\1\u0148",
            "\1\u0149\37\uffff\1\u0149",
            "\1\u014a\37\uffff\1\u014a",
            "\1\u014b\37\uffff\1\u014b",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u014d\37\uffff\1\u014d",
            "\1\u014e\37\uffff\1\u014e",
            "\1\u014f\37\uffff\1\u014f",
            "\1\u0150\37\uffff\1\u0150",
            "",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\1\u0152\37\uffff\1\u0152",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0154\37\uffff\1\u0154",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0156\37\uffff\1\u0156",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0159\37\uffff\1\u0159",
            "\1\137\1\uffff\12\u015a\7\uffff\6\136\32\uffff\6\136",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\12\u015c\7\uffff\6\u015c\32\uffff\6\u015c",
            "\1\u015d\37\uffff\1\u015d",
            "\1\u015e\37\uffff\1\u015e",
            "",
            "",
            "\1\u015f\37\uffff\1\u015f",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0161\37\uffff\1\u0161",
            "\1\u0162\37\uffff\1\u0162",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0164\37\uffff\1\u0164",
            "",
            "\1\u0165\37\uffff\1\u0165",
            "\1\u0166\37\uffff\1\u0166",
            "\1\u0167\37\uffff\1\u0167",
            "\1\u0168\37\uffff\1\u0168",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\137\1\uffff\12\u016d\7\uffff\6\136\32\uffff\6\136",
            "",
            "\1\136",
            "\1\u016e\37\uffff\1\u016e",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u016f\37\uffff\1\u016f",
            "",
            "\1\u0170\37\uffff\1\u0170",
            "\1\u0171\37\uffff\1\u0171",
            "",
            "\1\u0172\37\uffff\1\u0172",
            "\1\u0173\37\uffff\1\u0173",
            "\1\u0174\37\uffff\1\u0174",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "",
            "",
            "",
            "\1\136\1\137\1\uffff\12\47",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0178\37\uffff\1\u0178",
            "\1\u0179\37\uffff\1\u0179",
            "\1\u017a\37\uffff\1\u017a",
            "\1\u017b\37\uffff\1\u017b",
            "\1\u017c\37\uffff\1\u017c",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "",
            "",
            "\1\u017e\37\uffff\1\u017e",
            "\1\u017f\37\uffff\1\u017f",
            "\1\u0180\37\uffff\1\u0180",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0182\37\uffff\1\u0182",
            "",
            "\1\u0183\37\uffff\1\u0183",
            "\1\u0184\37\uffff\1\u0184",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            ""
    };

    static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
    static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
    static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
    static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
    static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
    static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
    static final short[][] DFA14_transition;

    static {
        int numStates = DFA14_transitionS.length;
        DFA14_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
        }
    }

    class DFA14 extends DFA {

        public DFA14(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 14;
            this.eot = DFA14_eot;
            this.eof = DFA14_eof;
            this.min = DFA14_min;
            this.max = DFA14_max;
            this.accept = DFA14_accept;
            this.special = DFA14_special;
            this.transition = DFA14_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | K_SELECT | K_FROM | K_WHERE | K_AND | K_KEY | K_INSERT | K_UPDATE | K_WITH | K_LIMIT | K_USING | K_CONSISTENCY | K_LEVEL | K_USE | K_COUNT | K_SET | K_BEGIN | K_APPLY | K_BATCH | K_TRUNCATE | K_DELETE | K_IN | K_CREATE | K_KEYSPACE | K_COLUMNFAMILY | K_INDEX | K_ON | K_DROP | K_PRIMARY | K_INTO | K_VALUES | K_TIMESTAMP | K_TTL | K_ALTER | K_ADD | K_TYPE | K_COMPACT | K_STORAGE | K_ORDER | K_BY | K_ASC | K_DESC | K_CLUSTERING | K_ASCII | K_BIGINT | K_BLOB | K_BOOLEAN | K_COUNTER | K_DECIMAL | K_DOUBLE | K_FLOAT | K_INT | K_TEXT | K_UUID | K_VARCHAR | K_VARINT | K_TIMEUUID | K_TOKEN | K_WRITETIME | STRING_LITERAL | QUOTED_NAME | INTEGER | QMARK | FLOAT | IDENT | UUID | WS | COMMENT | MULTILINE_COMMENT );";
        }
    }
 

}