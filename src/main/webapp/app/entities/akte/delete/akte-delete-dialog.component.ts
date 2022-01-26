import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAkte } from '../akte.model';
import { AkteService } from '../service/akte.service';

@Component({
  templateUrl: './akte-delete-dialog.component.html',
})
export class AkteDeleteDialogComponent {
  akte?: IAkte;

  constructor(protected akteService: AkteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.akteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
