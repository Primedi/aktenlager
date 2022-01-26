import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AkteComponent } from '../list/akte.component';
import { AkteDetailComponent } from '../detail/akte-detail.component';
import { AkteUpdateComponent } from '../update/akte-update.component';
import { AkteRoutingResolveService } from './akte-routing-resolve.service';
import { AkteSummeComponent } from '../summe/akte-summe.component';

const akteRoute: Routes = [
  {
    path: '',
    component: AkteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AkteDetailComponent,
    resolve: {
      akte: AkteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AkteUpdateComponent,
    resolve: {
      akte: AkteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AkteUpdateComponent,
    resolve: {
      akte: AkteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'summe',
    component: AkteSummeComponent,
    resolve: {
      akte: AkteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(akteRoute)],
  exports: [RouterModule],
})
export class AkteRoutingModule {}
