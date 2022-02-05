import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAkte } from '../akte.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AkteService } from '../service/akte.service';
import { AkteDeleteDialogComponent } from '../delete/akte-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-akte',
  templateUrl: './akte.component.html',
})
export class AkteComponent implements OnInit {
  aktes: IAkte[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  gesamtmeter: number;
  haengend: number;
  notHaengend: number;

  constructor(protected akteService: AkteService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.aktes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.gesamtmeter = 0;
    this.getGesamtmeter();
    this.haengend = 0;
    this.getHaengend();
    this.notHaengend = 0;
    this.getNotHaengend();
  }

  loadAll(): void {
    this.isLoading = true;

    this.akteService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IAkte[]>) => {
          this.isLoading = false;
          this.paginateAktes(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  getGesamtmeter(): void {
    this.akteService.getGesamtmeter().subscribe(({ body }) => {
      this.gesamtmeter = body!.aktenMeter ?? 0;
    });
  }

  getHaengend(): void {
    this.akteService.getHaengend().subscribe(({ body }) => {
      this.haengend = body!.aktenMeter ?? 0;
    });
  }

  getNotHaengend(): void {
    this.akteService.getNotHaengend().subscribe(({ body }) => {
      this.notHaengend = body!.aktenMeter ?? 0;
    });
  }

  reset(): void {
    this.page = 0;
    this.aktes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAkte): number {
    return item.id!;
  }

  delete(akte: IAkte): void {
    const modalRef = this.modalService.open(AkteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.akte = akte;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAktes(data: IAkte[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.aktes.push(d);
      }
    }
  }
}
