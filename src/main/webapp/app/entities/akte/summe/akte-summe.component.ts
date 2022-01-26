import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISumme } from '../summe.model';

@Component({
  selector: 'jhi-akte-summe',
  templateUrl: './akte-summe.component.html',
})
export class AkteSummeComponent implements OnInit {
  summe: ISumme | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ summe }) => {
      this.summe = summe;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
