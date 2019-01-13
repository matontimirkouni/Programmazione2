(*PR2 - Matonti Gioacchino Mirko*)

(* Identificatore *)
type ide = string;;

(* Espressioni *)
type exp =
	| Ide of ide
	| EInt of int
	| EString of ide
	| Add of exp * exp
	| Minus of exp * exp
	| Times of exp * exp
	| EBool of bool
	| Eq of exp * exp
	| Not of exp
	| And of exp * exp
	| Ifthenelse of exp * exp * exp
	| Let of ide * exp * exp
	| Letrec of ide * ide * exp * exp
	| Fun of ide * exp 
	| FunCall of exp * exp
	| Dict of (ide * exp ) list
	| Get of exp * ide
	| Put of exp * ide * exp
	| Rm of exp * ide
	| Clear of exp
	| ApplyOver of exp * exp;;


(*AMBIENTE*)

(*Ambiente vuoto*)
let emptyEnv = [];;

(*binding*)
let bind amb id vl = (id,vl)::amb;;

(*Ricerca valore nell ambiente *)
let rec lookup var env = match env with
	|(nome,valore)::env1 -> if nome = var then valore else lookup var env1
	|_ -> failwith "errore ambiente";;


(*AMBIENTE*)
type env = (ide * evT) list

(* Tipi *)
and evT = 
    | Int of int
    | Bool of bool
    | Funval of ide * exp *  env 
    | RecFunval of ide * ide * exp * env 
    | Dictval of (ide * evT) list
    | String of string
    | Unbound ;;

(* Type checking*)
let typecheck (x,y) = match x with 
	|"int" -> (match y with
	           |Int(_) -> true
	           | _ -> false)
	|"bool" -> (match y with 
				|Bool(u) -> true
				| _ -> false)
	|"dict" -> (match y with
				|Dictval(_) -> true
				|_ -> false)
	|"String" -> (match y with
				|String(_) -> true
				|_ -> false)
	|_ -> failwith ("tipo non valido");;
(* val typecheck : string * evT -> bool = <fun> *)


(* Add *)
let eval_add (x,y) = 
	match (typecheck("int",x),typecheck("int",y),x,y) with
	| (true,true,Int(x),Int(y)) -> Int (x+y)
	| (_,_,_,_) -> failwith "errore di tipo";;
(*val eval_add : evT * evT -> evT = <fun>*)

(* Minus *)
let eval_minus (x,y) = 
	match (typecheck("int",x),typecheck("int",y),x,y) with
	| (true,true,Int(x),Int(y)) -> Int (x - y)
	| (_,_,_,_) -> failwith "errore di tipo";;

(* Times *)
let eval_times (x,y) = 
	match (typecheck("int",x),typecheck("int",y),x,y) with
	| (true,true,Int(x),Int(y)) -> Int (x * y)
	| (_,_,_,_) -> failwith "errore di tipo";;

(* Div *)
let eval_div (x,y) = 
	match (typecheck("int",x),typecheck("int",y),x,y) with
	| (true,true,Int(x),Int(y)) -> Int (x / y)
	| (_,_,_,_) -> failwith "errore di tipo";;

(* Equal *)
let eq x y = 
	match (typecheck("int",x),typecheck("int",y),x,y) with
	| (true,true,Int(x),Int(y)) -> Bool(x=y)
	| (_,_,_,_) -> failwith "errore di tipo";;


(* interprete *)
let rec eval (e:exp) (r: env ) : evT =
	match e with
		|EInt i ->  Int i
		|Ide i -> lookup i r
		|EString i -> String i
		|EBool i-> Bool i
		|Eq(exp0,exp1) -> eq (eval exp0 r) (eval exp1 r)
		|Not(exp0) -> (match eval exp0 r with
							| Bool(true) -> Bool(false)
							| Bool(false) -> Bool(true)
							| _ -> failwith "errore")
		|And(exp1,exp2) -> (match (eval exp1 r,eval exp2 r) with
								| (Bool(true), Bool(true)) ->  Bool(true)
								| (_,_) ->  Bool(false))
		|Add(exp0,exp1) ->  eval_add (eval exp0 r , eval exp1 r)
		|Minus(exp0,exp1) -> eval_minus (eval exp0 r , eval exp1 r)
		|Times(exp0,exp1) -> eval_times (eval exp0 r , eval exp1 r)
		|Ifthenelse(g,exp0,exp1) ->( match (eval g r) with
									|Bool(true) -> eval exp0 r
									|Bool(false) -> eval exp1 r
									|_ -> failwith "errore" )
		|Let (i,dich,body) -> 
				let ival = eval dich r in 
					let env1 = bind r i ival in
						eval body env1
		|Letrec (i,pfun,fbody,lbody) -> 
				let env1 = bind r i (RecFunval(i,pfun,fbody,r)) in
					eval lbody env1	
		|Fun(i,exp0) -> Funval(i,exp0,r)
	    |FunCall(f,par) -> (match (eval f r) with 
	    						|Funval(i,fbody,fenv) ->
	    							let parval= eval par r in 
	    								let env1 = bind fenv i parval in
	    									eval fbody env1
	    						|RecFunval(i,fpar,fbody,fenv) ->
	    							let parval= eval par r in
	    								let rEnv= bind fenv i (eval f r) in
	    									let aEnv= bind rEnv fpar parval in 
	    										eval fbody aEnv
	    						|_ -> failwith "errore")
	    (* Estensione progetto*)
	    |Dict(ls) -> Dictval(evallst ls r)
	    |Get(dict,key) -> (match (eval dict r ) with
	    		| Dictval(lst) -> findandreturn lst key
	    		| _ -> failwith "errore")
	    |Put(dict,key,value) -> (match (eval dict r ) with
	    		| Dictval(lst) -> if (check lst key ) then 
	    							failwith "errore elemento già presente"
	    						  else Dictval((key,(eval value r))::lst)
	    		| _ -> failwith "errore")
	    |Rm(exp0,i) -> (match (eval exp0 r ) with
	    		| Dictval(lst) -> Dictval(removeitem lst i)
	    		| _ -> failwith "errore")
	    |Clear(exp0) -> (match (eval exp0 r ) with
	    		| Dictval(lst) -> Dictval([])
	    		| _ -> failwith "errore")
	    |ApplyOver(exp0,exp1) -> (match (eval exp1 r ) with
	    			| Dictval(lst) -> Dictval(applyF lst exp0 r)
	    			| _ -> failwith "errore")


(* ============  Funzioni ausiliare  ============= *)
(* costruisce la lista *)      
and  evallst (ls : (ide * exp) list) (r:  env) : (ide * evT) list =
         match ls with
 		 | (i,exp0)::xs -> if(check2 xs i) then evallst xs r
 		 					else (i,eval exp0 r) :: evallst xs r
         		
         | [] -> []

(* rimuove elemento *)        
and removeitem (ls : (ide * evT) list) (item : ide) : (ide * evT) list =
		 match ls with
		 |(i,e)::xs -> if (i = item) then xs
		 			   else (i,e)::removeitem xs item
		 |[] -> []

(* applica funzione f al dizionario*)
and applyF (ls:(ide * evT) list) f (r: env) = 
	match ls with  
	|(den,exp0)::xs  -> let tfun = eval f r in
				  (match tfun  with
					|Funval(i,fbody,fenv) -> let env1 = bind fenv i exp0 in
											 let resu = (eval fbody env1) in
											 (den,resu)::applyF xs f env1
					|RecFunval(i,fpar,fbody,fenv) ->
	    								let rEnv= bind fenv i tfun in
	    									let aEnv= bind rEnv fpar exp0 in 
	    										let resu = eval fbody aEnv in
	    											(den,resu)::applyF xs f aEnv
					| _ -> failwith "errore")
	|[] -> []

(* controlla se un elemento con ide i è presente *)	
and check (ls:(ide * evT) list) (i:ide) : bool = 
	match ls with  
	|(den,_)::xs  -> if (den = i) then true else check xs i
	|[] -> false


(* controlla se un elemento con ide i è presente *)	
and check2 (ls:(ide * exp) list) (i:ide) : bool = 
	match ls with  
	|(den,_)::xs  -> if (den = i) then true else check2 xs i
	|[] -> false


(* cerca e ritorna un elemento del dizionario*)
and findandreturn (ls:(ide * evT) list) (i:ide): evT = 
	match ls with 
    |(den,key) :: xs -> if (den = i ) then key else findandreturn xs i
    |[] -> failwith "non trovato";;




(* =============================  TESTS  GENERALI  ================= *)

(* ambiente vuoto *)
let env0 = emptyEnv;;

(* test and *)
let a1 = And(EBool true, EBool true);;
eval a1 env0;;

(* basico: let *)
let a3 = Let("x", EInt 1, Ide "x");;
eval a3 env0;;

(* complesso: let *)
let a4 = Let("x", EBool false, 
						Let("y", EBool false,
							Let("z", EBool true, Ifthenelse(Ide "x", Ide "y", Ide "z"))));;
eval a4 env0;;

(* funzione : y +1 *)
let f1 = Fun("y", Add(Ide("y"),EInt(1)));;
eval (FunCall(f1,EInt(3))) env0 ;;

(* =============================  TESTS  DIZIONARIO  ================= *)

(* Dizionario vuoto *)
let d0 = Dict([]);;
eval d0 env0;;

(* Dizionario : Put e Get *)
let d1 = Let("myDict", d0 , 
				Let("myDict2",Put(Ide("myDict"), "matricola", EInt 555555  ),
						Let("item", Get(Ide("myDict2"),"matricola") ,
							Ide("item"))));;
eval d1 env0;;

(* Dizionario polimorfo con duplicati*)
let d11 = Dict([("username",EString "mario") ; ("matricola",EInt 111111) ; ("username" , EString "rossi") ;]);;
eval d11 env0;;

(* Dizionario polimorfo *)
let d2 = Dict([("username",EString "mario") ; ("matricola",EInt 111111) ; ("cognome" , EString "rossi") ; ("studente" , EBool true)]);;
eval d2 env0;;

(* Dizionario get *)
let g1 = Let("myDict", d2 , Get(Ide("myDict"),"username"));;
eval g1 env0;;

(* Dizionario get *)
let g2 = Let("myDict", d2 , Get(Ide("myDict"),"studente"));;
eval g2 env0;;

(* Dizionario rimozione *)
let r1 = Let("myDict", d2 , Rm(Ide("myDict"),"username"));;
eval r1 env0;;

(* Dizionario clear *)
let c1 = Let("myDict", d2 , Let("myDict2",Clear(Ide "myDict"),Ide "myDict2"));;
eval c1 env0;;

(* Dizionario ApplyOver + errore typecheck *)
let f0 = Let("myDict", d2 , ApplyOver(Fun("y", Add(Ide "y", EInt 1)), d2 ));;
eval f0 env0;;

(* Nuovo dizionario *)
let d3 = Dict([("matricola",EInt 111111) ; ("eta",EInt 20) ;  ("anno" , EInt 2018)]);;
eval d3 env0;;

(* Dizionario ApplyOver  *)
let f1 = Let("myDict", d3 , ApplyOver(Fun("y", Add(Ide "y", EInt 1)), d3 ));;
eval f1 env0;;

(* Dizionario ApplyOver *)
let f2 = Let("myDict", d3 , ApplyOver(Fun("y", Times(Ide "y", EInt 5)), Ide "myDict"));;
eval f2 env0;;


(* Dizionario ApplyOver ricorsiva *)
let d3 = Dict([("eta",EInt 5)]);;

(* funzione fattoriale ricorsiva : *)
let fric= Let("myDict",d3,
				Letrec("fact","n",
				 			Ifthenelse(Eq(Ide("n"),EInt(0)),EInt(1),
				 					Times(Ide("n"),FunCall(Ide("fact"),Minus(Ide("n"),EInt(1))))),
				 		  				ApplyOver(Ide "fact",Ide "myDict" )));;

eval fric env0;;