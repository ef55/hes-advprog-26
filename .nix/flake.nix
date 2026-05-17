{
  description = "Melange starter";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs";
    flake-utils.url = "github:numtide/flake-utils";
    slides-lib.url = "git+https://git.ef5.ch/Ef5/slides-lib?dir=.nix";
  };

  outputs = { self, nixpkgs, flake-utils, slides-lib }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in
      {
        devShells.default = pkgs.mkShell {
          nativeBuildInputs = with pkgs; [
            # Slides
            ocaml
            dune_3
            ocamlPackages.findlib
            ocamlPackages.ocaml-lsp
            ocamlPackages.melange
            slides-lib.packages.${system}.slides
            slides-lib.packages.${system}.server
            deno

            # Scala code
            scala-next
          ];
          buildInputs = with pkgs; [ ocamlPackages.melange ];
        };
      });
}
