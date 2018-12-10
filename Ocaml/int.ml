

(* Operazioni *)
type op = Plus | Minus | Times | Div ;;

type ide = string;;

(* Espressioni *)
type exp =
	| Ide of ide
	| Int of int
	| Op_e of exp * op * exp
	| Let of ide * exp * exp
	| Add of exp * exp
	| Minus of exp * exp
	| Times of exp * exp
	| True 
	| False
	| Not of exp
	| And of exp * exp;;


(* eccezioni *)
exception Unbound;;



(* interprete op *)
let evalop (v1:exp) (ope:op) (v2:exp) : exp =
	match v1, ope, v2 with
	| Int i , Plus, Int j -> Int (i+j)
	| Int i , Minus, Int j -> Int (i-j)
	| Int i , Times, Int j -> Int (i * j)
	| Int i , Div, Int j -> Int (i / j)
	| _ -> raise Unbound;;

(* Casting  *)
let asint = function Int x -> x | _ -> failwith "not integer";;

let asstring = function Ide a -> a | _ -> failwith "not string";;

(* interprete *)
let rec eval  e =
	match e with
		| Int i -> Int i
		| Ide i -> Ide i (* Attenzione non corretto va fatto il bind *)
		| True -> True
		| False -> False
		| Not(exp0) -> (match eval exp0 with
							| True -> False
							| False -> True
							| _ -> raise Unbound)
		| And(exp1,exp2) -> (match (eval exp1,eval exp2) with
								| (True,True) -> True
								| (_,_) -> False)
		| Op_e(exp0,op0,exp1) -> let ev1 = eval exp0 in
								let ev2 = eval exp1 in
								evalop ev1 op0 ev2
		| Add(exp0,exp1) -> Int(asint(eval exp0) +  asint(eval exp1))
		| Minus(exp0,exp1) -> Int(asint(eval exp0) -  asint(eval exp1))
		| Times(exp0,exp1) -> Int(asint(eval exp0) *  asint(eval exp1));; 




let p = eval(And(Not(True),True));;

let p3= Op_e(Int(5),Times,Int(10));;
let p4= Op_e(Int(5),Div,Int(10));;
let p5 = Add(Int(5),Int(10));;
let p6 = Times(Int(5),Int(10));;
let p7 = Times(Ide("lol"),Ide("lols"));;
eval p3;;

									