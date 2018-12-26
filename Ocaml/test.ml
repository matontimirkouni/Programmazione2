(*
	DOMANDA 1) astrazione
*)
type ide = string;;

type exp = 
	| Ide of ide
	| Value of int
	| And of exp * exp
	| Or of exp * exp
	| Not of exp
	| Add of exp * exp
	| Sub of exp * exp
	| Mul of exp * exp
	| Equ of exp * exp
	| Leq of exp * exp
	| Geq of exp * exp
	| IfThenElse of exp * exp * exp
	| Let of ide * exp * exp
	| Pattern of pattern
	| TryWithIn of ide * exp * exp
	| Function of ide * ide * exp
	| FunCall of ide * exp

and pattern = (exp * exp) list
;;


(*eccezioni*)
exception WrongMatchException;;
exception WrongPatternException;;
exception EmptyEnvException;;

(*
	ambiente:
	emptyEnv : 'a -> 'b
	emptyFunEnv : 'a -> 'b
	bind : (ide -> 'a) -> ide -> 'a -> ide -> 'a
*)
let emptyEnv = fun x -> raise EmptyEnvException;;
let emptyFunEnv = fun x -> raise EmptyEnvException;;
let bind env (variable: ide) value = fun y -> if variable = y then value else env y;;

(*
	DOMANDA 2) semantica:
	eval : exp -> (ide -> int) -> 'a -> int
	getE : pattern -> (ide -> int) -> 'a -> int
	funDeclr : exp -> 'a -> (ide -> ide * exp * 'a) -> ide -> ide * exp * 'a
*)

let rec eval (expression: exp) env fenv = match expression with
	| Ide variable -> env variable
	| Value i -> i
	| Add (e1, e2) -> (eval e1 env fenv) + (eval e2 env fenv)
	| Sub (e1, e2)	->	(eval e1 env fenv) - (eval e2 env fenv)
	| Mul (e1, e2)	->	(eval e1 env fenv) * (eval e2 env fenv)
	| Equ (e1, e2)	->	if(eval e1 env fenv) = (eval e2 env fenv) then 1 else 0
	| Leq (e1, e2)	->	if(eval e1 env fenv) <= (eval e2 env fenv) then 1 else 0
	| Geq (e1, e2)	->	if(eval e1 env fenv) >= (eval e2 env fenv) then 1 else 0
	| And (e1, e2) 	->	if(eval e1 env fenv) != 0 then eval e2 env fenv else 0
	| Or (e1, e2) 	->	if(eval e1 env fenv) == 0 then 0 else eval e2 env fenv 
	| Not (e1) 		->	if(eval e1 env fenv) == 0 then 1 else 0	
	| IfThenElse(g, e1, e2) -> if(eval g env fenv) == 1
								then eval e1 env fenv
								else eval e2 env fenv
	| Let (variable, e, body) -> 
			let value = eval e env fenv in
				let env1 = bind env variable value in
					eval body env1 fenv
	| TryWithIn (variable, variable_val, pattern) -> 
			let value = eval variable_val env fenv in
				let env1 = bind env variable value in
					eval pattern env1 fenv
	| Pattern (p) -> getE p env fenv
	| FunCall (funName, arg) -> 
		let value = eval arg env fenv in
			let (param, body, ambiente) = fenv funName in
				let env1 = bind env param value in
					eval body env1 fenv
	| _ -> raise WrongMatchException

and getE p env fenv = match p with
	| [] -> raise WrongPatternException
	| (e1,e2)::rest -> if(eval e1 env fenv) == 1 then eval e2 env fenv else getE rest env fenv
;;

let funDeclr (expression: exp) env fenv = match expression with
	| Function (funName, param, body) -> bind fenv funName (param, body, env)
	| _ -> raise WrongMatchException
;;

(*
	****************************************
					TEST
	****************************************
*)


	(*DOMANDA 3) test *)

	let simpleAnd = And(Value 1, Value 1);;
	let doubleAnd = And(Value 1, And(Value 1, Value 0));;
	eval simpleAnd emptyEnv emptyFunEnv;;
	eval doubleAnd emptyEnv emptyFunEnv;;

	let simpleLet = Let("x", Value 1, Not(Ide "x"));;
	eval simpleLet emptyEnv emptyFunEnv;;

	let simpleExpression = Let("x", Value 1, IfThenElse(Ide "x", Value 20, Value 30));;
	eval simpleExpression emptyEnv emptyFunEnv;;

	let multipleLet = Let("x", Value 1, 
							Let("y", Value 10,
							Let("z", Value 5, IfThenElse(Ide "x", Ide "y", Ide "z"))));;

	eval multipleLet emptyEnv emptyFunEnv;;

	(*test operazioni*)

	let addxy = Let("x", Value 10, Let("y", Value 2, Add(Ide "x", Ide "y")));;
	eval addxy emptyEnv emptyFunEnv;;

	(*semplice script test:
		int x = 3 + 2
		if(x == 5) then 1 else 0
	*)

	let guardia = Equ(Ide "x", Value 5);;
	let e2 = Let("x", Add(Value 3, Value 2), IfThenElse(guardia, Value 1, Value 0));;	
	eval e2 emptyEnv emptyFunEnv;;

	(* script test
	int i = 2
	if(i == 10 - 6 || i == 3) then 100 else 0
	*)

	let guardia = Or(Equ(Ide "i", Sub(Value 10, Value 6)), Equ(Ide "i", Value 3));;
	let controllo = IfThenElse(guardia, Value 100, Value 0);;
	let body = Let("i", Value 2, controllo);;
	eval body emptyEnv emptyFunEnv;;

	(*semplice test try IDE with E in PATTERN*)
	let compare1 = Equ(Ide "x", Value 1);;
	let compare2 = Equ(Ide "x", Value 2);;
	let base = Equ(Value 1, Value 1);;
	let c1r = Value 1;;
	let c2r = Value 2;;
	let cbaser = Value 100;;
	let pattern = Pattern((compare1,c1r)::(compare2,c2r)::(base, cbaser)::[])
	let twp = TryWithIn("x", Value 1, pattern);; (*esiste match*)
	let twp2 = TryWithIn("x", Value 3, pattern);; (*avvia caso base*)

	eval twp emptyEnv emptyFunEnv;;
	eval twp2 emptyEnv emptyFunEnv;;

	(*funzioni*)
	(*semplice funzione:
		
		int inc(int x)
		{
			x = x +1
		}
		
		int i = 10;
		inc(10)
	*)

	let inc = Function("inc", "x", Add(Ide "x", Value 1));;
	let fenv1 = funDeclr inc emptyEnv emptyFunEnv;;
	let var = Let("i", Value 10, FunCall("inc", Ide "i"));;
	eval var emptyEnv fenv1;;

	(*test presente sul pdf progetto
		
		try x with +(z,w) in
		(x > 0 --> succ(x))::
		(x < 0 --> abs(x))::
		(_ --> x)
	
	*)
	let succ = Function("succ", "i", Add(Ide "i", Value 1));;
	let abs = Function("abs", "i", Sub(Value 0, Ide "i"));;
	let fenv1 = funDeclr succ emptyEnv emptyFunEnv;;
	let fenv2 = funDeclr abs emptyEnv fenv1;;

	let compare1 = And(Geq(Ide "x", Value 0), Not(Equ(Ide "x", Value 0)));;
	let compare2 = And(Leq(Ide "x", Value 0), Not(Equ(Ide "x", Value 0)));;
	let esp1 = FunCall("succ", Ide "x");;
	let esp2 = FunCall("abs", Ide "x");;
	let pattern = Pattern((compare1, esp1)::(compare2, esp2)::(Equ(Value 1, Value 1), Ide "x")::[]);;
	let twp = TryWithIn("x", Add(Ide "z", Ide "w"), pattern);;

	let start_succ_ris_5 = Let("z", Value 1, 
			Let("w", Value 3, twp));;

	eval start_succ_ris_5 emptyEnv fenv2;;

	let start_abs_ris_2 = Let("z", Value (-3), 
			Let("w", Value 1, twp));;

	eval start_abs_ris_2 emptyEnv fenv2;;

	let start_default_ris_0 = Let("z", Value 0, 
			Let("w", Value 0, twp));; 

	eval start_default_ris_0 emptyEnv fenv2;;	

(*
	DOMANDA 4)
	se dovessimo progettare un interprete con Scope Dinamico l'unica modifica da fare è non salvare
	l'ambiente ENV durante la dichiarazione delle funzioni. Cosi facendo l'invocazione della funzione 
	si basa sull'ambiente in cui è stata invocata e non in quella in cui è stata dichiarata.
*)