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
    });
  }

  previousState(): void {
    window.history.back();
  }
}
