import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RaumComponent } from '../list/raum.component';
import { RaumDetailComponent } from '../detail/raum-detail.component';
import { RaumUpdateComponent } from '../update/raum-update.component';
import { RaumRoutingResolveService } from './raum-routing-resolve.service';

const raumRoute: Routes = [
  {
    path: '',
    component: RaumComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RaumDetailComponent,
    resolve: {
      raum: RaumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RaumUpdateComponent,
    resolve: {
      raum: RaumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RaumUpdateComponent,
    resolve: {
      raum: RaumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(raumRoute)],
  exports: [RouterModule],
})
export class RaumRoutingModule {}
