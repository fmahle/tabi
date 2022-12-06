class CharTreeGraph{

    class CharTreeNode{
        public CharTreeNode[] children;     
        public char c;
        public boolean isEnd;
        public Token key;
        public int currentChild;
        public CharTreeNode(char c,boolean isEnd,Token key){
            this.c=c;
            this.isEnd=isEnd;
            children= new CharTreeNode[16];
            currentChild=0;
            if(isEnd){
                this.key=key;
            }
        }
        void addStringToGraph(String str,Token key){
            boolean hasFound= false;
            
            for(int i=0; i<currentChild;i++){
                if(children[i].c==str.charAt(0)){
                    hasFound=true;
                    str=str.substring(1);
                    children[i].addStringToGraph(str, key);
                }
            }
            if(!hasFound){
                int insertIndex=currentChild;
                if(currentChild==children.length){

                
                    
                    CharTreeNode[] newChildren= new CharTreeNode[children.length*2];
                    for(int i=0; i<children.length;i++){
                        newChildren[i]=children[i];
                    }
                    children=newChildren;
                    
                }
                
                children[insertIndex]= new CharTreeNode(str.charAt(0),str.length()==1,key);
                if(str.length()!=1){
                    str=str.substring(1);
                    children[insertIndex].addStringToGraph(str, key);
                }
                insertIndex++;
            }
           
        } 
        public Token searchForToken(String token){
            for(int i=0; i<this.currentChild;i++){
                
                if(children[i].c==token.charAt(0)){
                    //token is at the end
                    if(token.length()==1){
                        if(children[i].isEnd){
                            return children[i].key;
                        }else{
                            return null;
                        }
                    }else{
                        token=token.substring(1);
                        Token res= children[i].searchForToken(token);
                        if(res==null){
                            if(this.isEnd){
                                return this.key;
                            }else{
                                return null;
                            }
                        }
                    }
                }
            }
            //if no child was found
            if(this.isEnd){
                return this.key;
            }else{
                return null;
            }
        }  
    }
    private CharTreeNode node;
    public CharTreeGraph(){
        node= new CharTreeNode('a', false, null);

    }
    void addTokenToGraph(Token str){
        node.addStringToGraph(str.tokenName, str);
    }
    Token searchForToken(String tokenStr){
        	return node.searchForToken(tokenStr);
    }
}
