class CharTreeGraph {
    class CharTreeNode {
        public CharTreeNode[] children;     
        public char c;
        public boolean isEnd;
        public Token key;
        public int currentChild;
        public CharTreeNode(char c,boolean isEnd,Token key) {
            this.c = c;
            this.isEnd = isEnd;
            children= new CharTreeNode[16];
            currentChild = 0;
            if (isEnd) {
                this.key = key;
            }
        }
        void addStringToGraph(String str,Token key){
            boolean hasFound = false;
            
            for(int i = 0; i < currentChild; i++){
                if(children[i].c == str.charAt(0)){
                    hasFound = true;
                    if (str.length() > 1){
                        str = str.substring(1);
                        children[i].addStringToGraph(str, key);
                    }else{
                        children[i].isEnd=true;
                        children[i].key=key;
                        key.isValid=true;
                    }
                }
            }
            if (!hasFound) {
                 
                if (currentChild == children.length) {
                    
                    CharTreeNode[] newChildren = new CharTreeNode[children.length*2];
                    for(int i = 0; i < children.length; i++){
                        newChildren[i] = children[i];
                    }
                    children = newChildren;
                    
                }
                
                children[currentChild] = new CharTreeNode(str.charAt(0), str.length() == 1, key);
                if(str.length() != 1) {
                    str=str.substring(1);
                    children[currentChild].addStringToGraph(str, key);
                }
                currentChild++;
            }
        } 
        public Token searchForToken(String token) {
            for (int i = 0; i < this.currentChild; i++) {
                
                if(children[i].c == token.charAt(0)) {
                    //token is at the end
                    if(token.length()==1 ){
                        if(children[i].isEnd){
                            return children[i].key;
                        }
                        else {
                            return null;
                        }
                    }
                    else {
                        if(token.charAt(1)==' '){
                            if(children[i].isEnd){
                                return children[i].key;
                            }
                            else {
                                return null;
                            }
                        }else{
                            token = token.substring(1);
                            Token res = children[i].searchForToken(token);
                            return res;
                        }
                     
                        /* 
                        if (res == null) {
                            if (this.isEnd) {
                                return this.key;
                            }
                            else {
                                return null;
                            }
                        }
                        else {
                            return res;
                        }*/
                    }
                }
            }
            //if no child was found
            /*if (this.isEnd) {
                return this.key;
            }
            else {
                return null;
            }*/
            return null;
        }
        public Token removeTokenStringFast(String token){
            for (int i = 0; i < this.currentChild; i++) {
                
                if(children[i].c == token.charAt(0)) {
                    //token is at the end
                    if(token.length()==1){
                        if(children[i].isEnd){
                            children[i].isEnd=false;
                            children[i].key.isValid=false;
                            return children[i].key;
                        }
                         
                    }
                    else {
                        token = token.substring(1);
                        return children[i].removeTokenStringFast(token);
                        
                        
                    }
                }
            }
            return null;
        }  
    }
    private CharTreeNode node;

    public CharTreeGraph() {
        node = new CharTreeNode('a', false, null);
    }

    void addTokenToGraph(Token str,boolean applyValid) {
        str.isValid=applyValid;
        node.addStringToGraph(str.tokenName, str);
    }
    Token removeToken(Token t){
        if(t!=null){
            return node.removeTokenStringFast(t.tokenName);
        }else{
            return null;
        }
    }
    Token searchForToken(String tokenStr) {
        if(tokenStr.length() != 0) {
            return node.searchForToken(tokenStr);
        }
        else {
            return null;
        }
    }
}
