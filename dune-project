(lang dune 3.20)

(name hes-gadts)
(using melange 1.0)

(generate_opam_files true)

(source
 (uri "https://git.ef5.ch/Papers-and-Talks/HES26-GADTs"))

(license MIT)

(package
 (name hes-gadts)
 (allow_empty)
 (depends
  ocaml
  slides))