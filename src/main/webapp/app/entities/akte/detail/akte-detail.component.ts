import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAkte } from '../akte.model';

@Component({
  selector: 'jhi-akte-detail',
  templateUrl: './akte-detail.component.html',
})
export class AkteDetailComponent implements OnInit {
  akte: IAkte | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ akte }) => {
      this.akte = akte;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
