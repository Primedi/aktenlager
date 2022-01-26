import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AkteComponent } from './list/akte.component';
import { AkteDetailComponent } from './detail/akte-detail.component';
import { AkteUpdateComponent } from './update/akte-update.component';
import { AkteDeleteDialogComponent } from './delete/akte-delete-dialog.component';
import { AkteRoutingModule } from './route/akte-routing.module';

@NgModule({
  imports: [SharedModule, AkteRoutingModule],
  declarations: [AkteComponent, AkteDetailComponent, AkteUpdateComponent, AkteDeleteDialogComponent],
  entryComponents: [AkteDeleteDialogComponent],
})
export class AkteModule {}
