import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRaum } from '../raum.model';
import { RaumService } from '../service/raum.service';

@Component({
  templateUrl: './raum-delete-dialog.component.html',
})
export class RaumDeleteDialogComponent {
  raum?: IRaum;

  constructor(protected raumService: RaumService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.raumService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
