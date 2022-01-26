import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRaum, Raum } from '../raum.model';
import { RaumService } from '../service/raum.service';

@Component({
  selector: 'jhi-raum-update',
  templateUrl: './raum-update.component.html',
})
export class RaumUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    gebaeude: [],
    raumnummer: [],
    etage: [],
    zusatz: [],
  });

  constructor(protected raumService: RaumService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raum }) => {
      this.updateForm(raum);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const raum = this.createFromForm();
    if (raum.id !== undefined) {
      this.subscribeToSaveResponse(this.raumService.update(raum));
    } else {
      this.subscribeToSaveResponse(this.raumService.create(raum));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRaum>>): void {
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

  protected updateForm(raum: IRaum): void {
    this.editForm.patchValue({
      id: raum.id,
      gebaeude: raum.gebaeude,
      raumnummer: raum.raumnummer,
      etage: raum.etage,
      zusatz: raum.zusatz,
    });
  }

  protected createFromForm(): IRaum {
    return {
      ...new Raum(),
      id: this.editForm.get(['id'])!.value,
      gebaeude: this.editForm.get(['gebaeude'])!.value,
      raumnummer: this.editForm.get(['raumnummer'])!.value,
      etage: this.editForm.get(['etage'])!.value,
      zusatz: this.editForm.get(['zusatz'])!.value,
    };
  }
}
