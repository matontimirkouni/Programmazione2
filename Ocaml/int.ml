

(* Operazioni *)
type op = Plus | Minus | Times | Div ;;

type variable = string

(* Espressioni *)
type exp =
	| Int_e of int
	| Op_e of exp * op * exp
	| Var_e of variable
	| Let_e of variable * exp * exp
	| True 
	| False
	| Not of exp
	| And of exp * exp;;


(* eccezioni *)
exception Unbound;;



(* interprete op *)
let evalop (v1:exp) (ope:op) (v2:exp) : exp =
	match v1, ope, v2 with
	| Int_e i , Plus, Int_e j -> Int_e (i+j)
	| Int_e i , Minus, Int_e j -> Int_e (i-j)
	| Int_e i , Times, Int_e j -> Int_e (i * j)
	| Int_e i , Div, Int_e j -> Int_e (i / j)
	| _ -> raise Unbound;;


(* interprete *)
let rec eval  (e : exp) : exp  =
	match e with
		| Int_e i -> Int_e i
		| True -> True
		| False -> False
		| Not(exp0) -> (match eval exp0 with
							| True -> False
							| False -> True
							| _ -> raise Unbound)
		| And(exp1,exp2) -> (match (eval exp1,eval exp2) with
								| (True,True) -> True
								| (_,_) -> False)
		|Op_e(exp0,op0,exp1) -> let ev1 = eval exp0 in
								let ev2 = eval exp1 in
								evalop ev1 op0 ev2;;		

let p = eval(And(Not(True),True));;

let p3= Op_e(Int_e(5),Times,Int_e(10));;
let p4= Op_e(Int_e(5),Div,Int_e(10));;
eval p3;;

									