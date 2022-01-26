import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAkte, Akte } from '../akte.model';
import { AkteService } from '../service/akte.service';
import { IRaum } from 'app/entities/raum/raum.model';
import { RaumService } from 'app/entities/raum/service/raum.service';

@Component({
  selector: 'jhi-akte-update',
  templateUrl: './akte-update.component.html',
})
export class AkteUpdateComponent implements OnInit {
  isSaving = false;

  raumsSharedCollection: IRaum[] = [];

  editForm = this.fb.group({
    id: [],
    aktenthema: [],
    organisationsEinheit: [],
    aktenMeter: [],
    haengend: [],
    standort: [],
    raum: [],
  });

  constructor(
    protected akteService: AkteService,
    protected raumService: RaumService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ akte }) => {
      this.updateForm(akte);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const akte = this.createFromForm();
    if (akte.id !== undefined) {
      this.subscribeToSaveResponse(this.akteService.update(akte));
    } else {
      this.subscribeToSaveResponse(this.akteService.create(akte));
    }
  }

  trackRaumById(index: number, item: IRaum): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAkte>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(akte: IAkte): void {
    this.editForm.patchValue({
      id: akte.id,
      aktenthema: akte.aktenthema,
      organisationsEinheit: akte.organisationsEinheit,
      aktenMeter: akte.aktenMeter,
      haengend: akte.haengend,
      standort: akte.standort,
      raum: akte.raum,
    });

    this.raumsSharedCollection = this.raumService.addRaumToCollectionIfMissing(this.raumsSharedCollection, akte.raum);
  }

  protected loadRelationshipsOptions(): void {
    this.raumService
      .query()
      .pipe(map((res: HttpResponse<IRaum[]>) => res.body ?? []))
      .pipe(map((raums: IRaum[]) => this.raumService.addRaumToCollectionIfMissing(raums, this.editForm.get('raum')!.value)))
      .subscribe((raums: IRaum[]) => (this.raumsSharedCollection = raums));
  }

  protected createFromForm(): IAkte {
    return {
      ...new Akte(),
      id: this.editForm.get(['id'])!.value,
      aktenthema: this.editForm.get(['aktenthema'])!.value,
      organisationsEinheit: this.editForm.get(['organisationsEinheit'])!.value,
      aktenMeter: this.editForm.get(['aktenMeter'])!.value,
      haengend: this.editForm.get(['haengend'])!.value,
      standort: this.editForm.get(['standort'])!.value,
      raum: this.editForm.get(['raum'])!.value,
    };
  }
}
