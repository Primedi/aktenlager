import { IAkte } from 'app/entities/akte/akte.model';

export interface IRaum {
  id?: number;
  gebaeude?: string | null;
  raumnummer?: number | null;
  etage?: number | null;
  zusatz?: string | null;
  aktes?: IAkte[] | null;
}

export class Raum implements IRaum {
  constructor(
    public id?: number,
    public gebaeude?: string | null,
    public raumnummer?: number | null,
    public etage?: number | null,
    public zusatz?: string | null,
    public aktes?: IAkte[] | null
  ) {}
}

export function getRaumIdentifier(raum: IRaum): number | undefined {
  return raum.id;
}
