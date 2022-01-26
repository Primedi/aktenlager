import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RaumComponent } from './list/raum.component';
import { RaumDetailComponent } from './detail/raum-detail.component';
import { RaumUpdateComponent } from './update/raum-update.component';
import { RaumDeleteDialogComponent } from './delete/raum-delete-dialog.component';
import { RaumRoutingModule } from './route/raum-routing.module';

@NgModule({
  imports: [SharedModule, RaumRoutingModule],
  declarations: [RaumComponent, RaumDetailComponent, RaumUpdateComponent, RaumDeleteDialogComponent],
  entryComponents: [RaumDeleteDialogComponent],
})
export class RaumModule {}
