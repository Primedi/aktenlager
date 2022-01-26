import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AkteSummeComponent } from './summe/akte-summe.component';

@NgModule({
  imports: [SharedModule],
  declarations: [AkteSummeComponent],
})
export class SummeModule {}
