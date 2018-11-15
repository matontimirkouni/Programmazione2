let rec max_list = function
	| [] -> None
	| h::t -> match max_list t with
		| None -> Some h
		| Some x -> Some (max h x) ;;


type expr =
	Lit of int
	| Plus of expr * expr
	| Minus of expr * expr
	| Times of expr * expr;;

let rec eval = function
	| Lit(x) -> x
	| Plus(e1,e2) -> (eval e1) + (eval e2);;
