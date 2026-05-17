open Domext
open Slides.Lib

type api = {
  controls: controls;
}

external set_api: Dom.window -> api -> unit
  = "slides" [@@mel.set]

let setup_controls (ctr: controls): unit =
  add_event_listener "keyup" (fun e_ ->
    let e: Dom.keyboardEvent = Obj.magic e_ in
    begin match get_key e with
    | "ArrowLeft" -> ctr.backward ()
    | "ArrowRight" -> ctr.forward ()
    | _ -> ()
    end
  );
  add_event_listener "hashchange" (fun _ ->
    int_of_string_opt (get_hash ()) |> Option.value ~default:0 |> ctr.goto;
  );
  add_event_listener "beforeprint" (fun _ -> ctr.set_mode Print);
  add_event_listener "afterprint" (fun _ -> ctr.set_mode Presentation)

let init () =
  let data = Belt.Map.String.fromArray [|
    "speaker", "No&eacute; De Santo"
  |]
  in

  let root = get_element_by_id document "slides" in
  let ctrl = init root data in
  setup_controls ctrl;
  set_api window { controls = ctrl };
  print_endline ("Slideshow set in: " ^ (get_element_id root))

let () =
  init ()