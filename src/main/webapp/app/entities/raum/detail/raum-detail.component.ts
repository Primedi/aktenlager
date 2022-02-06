import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRaum } from '../raum.model';

@Component({
  selector: 'jhi-raum-detail',
  templateUrl: './raum-detail.component.html',
})
export class RaumDetailComponent implements OnInit {
  raum: IRaum | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raum }) => {
      this.raum = raum;
      let r: string | number | null | undefined = '0';
      r = this.raum?.raumnummer;
      if (r) {
        if (r.valueOf() < 10) {
          this.raum!.raumFuehrendeNull = `0${r.valueOf()}`;
        } else {
          this.raum!.raumFuehrendeNull = `this.raum!.raumnummer`;
        }
      }
    });
  }

  previousState(): void {
    window.history.back();
  }
}
