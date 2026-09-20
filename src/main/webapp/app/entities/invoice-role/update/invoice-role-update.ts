import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInvoiceRole } from '../invoice-role.model';
import { InvoiceRoleService } from '../service/invoice-role.service';

import { InvoiceRoleFormGroup, InvoiceRoleFormService } from './invoice-role-form.service';

@Component({
  selector: 'sys-invoice-role-update',
  templateUrl: './invoice-role-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InvoiceRoleUpdate implements OnInit {
  readonly isSaving = signal(false);
  invoiceRole: IInvoiceRole | null = null;

  invoicesSharedCollection = signal<IInvoice[]>([]);
  invoiceItemsSharedCollection = signal<IInvoiceItem[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);

  protected invoiceRoleService = inject(InvoiceRoleService);
  protected invoiceRoleFormService = inject(InvoiceRoleFormService);
  protected invoiceService = inject(InvoiceService);
  protected invoiceItemService = inject(InvoiceItemService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceRoleFormGroup = this.invoiceRoleFormService.createInvoiceRoleFormGroup();

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  compareInvoiceItem = (o1: IInvoiceItem | null, o2: IInvoiceItem | null): boolean => this.invoiceItemService.compareInvoiceItem(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceRole }) => {
      this.invoiceRole = invoiceRole;
      if (invoiceRole) {
        this.updateForm(invoiceRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const invoiceRole = this.invoiceRoleFormService.getInvoiceRole(this.editForm);
    if (invoiceRole.id === null) {
      this.subscribeToSaveResponse(this.invoiceRoleService.create(invoiceRole));
    } else {
      this.subscribeToSaveResponse(this.invoiceRoleService.update(invoiceRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInvoiceRole | null>): void {
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
    this.isSaving.set(false);
  }

  protected updateForm(invoiceRole: IInvoiceRole): void {
    this.invoiceRole = invoiceRole;
    this.invoiceRoleFormService.resetForm(this.editForm, invoiceRole);

    this.invoicesSharedCollection.update(invoices =>
      this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, invoiceRole.invoice),
    );
    this.invoiceItemsSharedCollection.update(invoiceItems =>
      this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, invoiceRole.invoiceItem),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, invoiceRole.party));
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, invoiceRole.roleType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) => this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, this.invoiceRole?.invoice)),
      )
      .subscribe((invoices: IInvoice[]) => this.invoicesSharedCollection.set(invoices));

    this.invoiceItemService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceItem[]>) => res.body ?? []))
      .pipe(
        map((invoiceItems: IInvoiceItem[]) =>
          this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, this.invoiceRole?.invoiceItem),
        ),
      )
      .subscribe((invoiceItems: IInvoiceItem[]) => this.invoiceItemsSharedCollection.set(invoiceItems));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.invoiceRole?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.invoiceRole?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));
  }
}
