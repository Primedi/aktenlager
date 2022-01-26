export interface ISumme {
  summe?: number;
}

export class Summe implements ISumme {
  constructor(public summe?: number) {
    this.summe = this.summe ?? 0;
  }
}
