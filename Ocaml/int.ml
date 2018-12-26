
(* Identificatore *)
type ide = string;;

(* Espressioni *)
type exp =
	| Ide of ide
	| EInt of int
	| Add of exp * exp
	| Minus of exp * exp
	| Times of exp * exp
	| EBool of bool
	| Not of exp
	| And of exp * exp
	| Ifthenelse of exp * exp * exp
	| Let of ide * exp * exp
	| Letrec of ide * exp * exp
	| Fun of ide * exp 
	| FunCall of exp * exp;;


(* eccezioni *)
exception Unbound;;


(* the empty environment *)
type 't env = ide -> 't;;
let emptyenv (v : 't) = function x -> v;;
let applyenv (r: 't env) (i:ide) = r i ;;
let bind (r: 't env) (i : ide) (v : 't) = 
				function x -> if x= i then v 
							else applyenv r x ;;
									

let rec lookup env x =
	match env with
	| [] -> failwith (" not found")
	| (y,v) :: r -> if x = y then v else lookup r x;;



(* Tipi *)
type evT = 
    | Int of int
    | Bool of bool
    | Funval of ide * exp * evT env 
    | Unbound ;;


(* Type checking*)
let typecheck (x,y) = match x with 
	|"int" -> (match y with
	           |Int(_) -> true
	           | _ -> false)
	|"bool" -> (match y with 
				|Bool(u) -> true
				| _ -> false)
	|_ -> failwith ("not valid");;
(* val typecheck : string * evT -> bool = <fun> *)


(* Add *)
let eval_add (x,y) = 
	match (typecheck("int",x),typecheck("int",y),x,y) with
	| (true,true,Int(x),Int(y)) -> Int (x+y)
	| (_,_,_,_) -> failwith "runtime error";;
(*val eval_add : evT * evT -> evT = <fun>*)

(* Minus *)
let eval_minus (x,y) = 
	match (typecheck("int",x),typecheck("int",y),x,y) with
	| (true,true,Int(x),Int(y)) -> Int (x - y)
	| (_,_,_,_) -> failwith "runtime error";;

(* Times *)
let eval_times (x,y) = 
	match (typecheck("int",x),typecheck("int",y),x,y) with
	| (true,true,Int(x),Int(y)) -> Int (x * y)
	| (_,_,_,_) -> failwith "runtime error";;

(* Div *)
let eval_div (x,y) = 
	match (typecheck("int",x),typecheck("int",y),x,y) with
	| (true,true,Int(x),Int(y)) -> Int (x / y)
	| (_,_,_,_) -> failwith "runtime error";;

(* interprete *)
let rec eval (e:exp) (r:evT env ) : evT =
	match e with
		| EInt i ->  Int i
		| Ide i -> applyenv r i 
		| Let (i,dich,body) -> 
				let ival = eval dich r in (*valuta le dichiarazioni*)
				let env1 = bind r i ival in
				eval body env1
		| EBool i-> Bool i
		| Not(exp0) -> (match eval exp0 r with
							| Bool(true) -> Bool(false)
							| Bool(false) -> Bool(true)
							| _ -> raise Unbound)
		| And(exp1,exp2) -> (match (eval exp1 r,eval exp2 r) with
								| (Bool(true), Bool(true)) ->  Bool(true)
								| (_,_) ->  Bool(false))
		| Add(exp0,exp1) ->  eval_add (eval exp0 r , eval exp1 r)
		| Minus(exp0,exp1) -> eval_add (eval exp0 r , eval exp1 r)
		| Times(exp0,exp1) -> eval_add (eval exp0 r , eval exp1 r)
		| Ifthenelse(g,exp0,exp1) -> match (eval g r) with
									|Bool(true) -> eval exp0 r
									|Bool(false) -> eval exp1 r
									|_ -> failwith "error" 
		|Fun(i,exp0) -> Funval(i,exp0,r)
	    |FunCall(i,exp0) -> match (eval i r1) with 
	    					|Funval(i,exp0,r) ->
	    						let exval= eval exp0 r in 
	    						let env1 = bind r1 i exval in
	    						eval exp0 env1

		| _ -> raise Unbound;;


(* test ifthenelse *)
let tss = Ifthenelse(EBool(false),EInt 4,EInt 3);;

(* test let prima *)
let p = eval (And(Not(EBool(false)),EBool(true))) env0;;

let p3= Op_e(Int(5),Times,Int(10));;
let p4= Op_e(Int(5),Div,Int(10));;
let p5 = Add(Int(5),Int(10));;
let p6 = Times(Int(5),Int(10));;
let p7 = Times(Ide("lol"),Ide("lols"));;



let env0 = emptyenv Unbound;;
(*let x = 5+5 in x *)
let ll = Let("x",Add(Int(5),Int(5)),Ide("x"));;
eval ll env0;;


eval(Let("x", Sum(Eint 1, Eint 0),
 Let("y", Ifthenelse(Eq(Den "x", Eint 0),
 Diff(Den "x", Eint 1),
 Sum(Den "x", Eint 1)),
 Let("z", Sum(Den "x", Den "y"), Den "z"))),
 (emptyenv Unbound));;

eval p3;;




(* Roba dizionario *)

type exp = 
	|Bool of bool
	|Int of int;;

let b = Bool true;;
let c = Int 4;;
let d = Int 8;;
let ls = d::c::b::[];;


let g x  = x+x ;;



(* funzione map *)
let rec f ls = match ls with 
      | e::xs -> (match e with 
      				|Int i -> let x1 = g i in
      				        x1::(f xs)
      				| _ ->  f xs)

      | [] -> []boo
      | _ ->  [];;


let pick  n =
  if n > 0 then ( fun x -> x +1) 
  	else (fun x -> x - 1 ) 
let g = (pick -5) ;;
g 6;;