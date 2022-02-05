import { IRaum } from 'app/entities/raum/raum.model';

export interface IAkte {
  id?: number;
  aktenthema?: string | null;
  organisationsEinheit?: string | null;
  aktenMeter?: number | null;
  haengend?: boolean | null;
  standort?: string | null;
  raum?: IRaum | null;
}

export class Akte implements IAkte {
  constructor(
    public id?: number,
    public aktenthema?: string | null,
    public organisationsEinheit?: string | null,
    public aktenMeter?: number | null,
    public haengend?: boolean | null,
    public standort?: string | null,
    public raum?: IRaum | null
  ) {
    this.haengend = this.haengend ?? false;
  }
}

export function getAkteIdentifier(akte: IAkte): number | undefined {
  return akte.id;
}
