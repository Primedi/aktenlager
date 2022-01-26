import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'akte',
        data: { pageTitle: 'Aktes' },
        loadChildren: () => import('./akte/akte.module').then(m => m.AkteModule),
      },
      {
        path: 'raum',
        data: { pageTitle: 'Raums' },
        loadChildren: () => import('./raum/raum.module').then(m => m.RaumModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
