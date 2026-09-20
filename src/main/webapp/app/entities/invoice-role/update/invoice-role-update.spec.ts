import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { IInvoiceRole } from '../invoice-role.model';
import { InvoiceRoleService } from '../service/invoice-role.service';

import { InvoiceRoleFormService } from './invoice-role-form.service';
import { InvoiceRoleUpdate } from './invoice-role-update';

describe('InvoiceRole Management Update Component', () => {
  let comp: InvoiceRoleUpdate;
  let fixture: ComponentFixture<InvoiceRoleUpdate>;
  let activatedRoute: ActivatedRoute;
  let invoiceRoleFormService: InvoiceRoleFormService;
  let invoiceRoleService: InvoiceRoleService;
  let invoiceService: InvoiceService;
  let invoiceItemService: InvoiceItemService;
  let partyService: PartyService;
  let roleTypeService: RoleTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(InvoiceRoleUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceRoleFormService = TestBed.inject(InvoiceRoleFormService);
    invoiceRoleService = TestBed.inject(InvoiceRoleService);
    invoiceService = TestBed.inject(InvoiceService);
    invoiceItemService = TestBed.inject(InvoiceItemService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Invoice query and add missing value', () => {
      const invoiceRole: IInvoiceRole = { id: 9582 };
      const invoice: IInvoice = { id: 1362 };
      invoiceRole.invoice = invoice;

      const invoiceCollection: IInvoice[] = [{ id: 1362 }];
      vi.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoice];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      vi.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceRole });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InvoiceItem query and add missing value', () => {
      const invoiceRole: IInvoiceRole = { id: 9582 };
      const invoiceItem: IInvoiceItem = { id: 29231 };
      invoiceRole.invoiceItem = invoiceItem;

      const invoiceItemCollection: IInvoiceItem[] = [{ id: 29231 }];
      vi.spyOn(invoiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceItemCollection })));
      const additionalInvoiceItems = [invoiceItem];
      const expectedCollection: IInvoiceItem[] = [...additionalInvoiceItems, ...invoiceItemCollection];
      vi.spyOn(invoiceItemService, 'addInvoiceItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceRole });
      comp.ngOnInit();

      expect(invoiceItemService.query).toHaveBeenCalled();
      expect(invoiceItemService.addInvoiceItemToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceItemCollection,
        ...additionalInvoiceItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const invoiceRole: IInvoiceRole = { id: 9582 };
      const party: IParty = { id: 24527 };
      invoiceRole.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceRole });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const invoiceRole: IInvoiceRole = { id: 9582 };
      const roleType: IRoleType = { id: 17576 };
      invoiceRole.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceRole });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const invoiceRole: IInvoiceRole = { id: 9582 };
      const invoice: IInvoice = { id: 1362 };
      invoiceRole.invoice = invoice;
      const invoiceItem: IInvoiceItem = { id: 29231 };
      invoiceRole.invoiceItem = invoiceItem;
      const party: IParty = { id: 24527 };
      invoiceRole.party = party;
      const roleType: IRoleType = { id: 17576 };
      invoiceRole.roleType = roleType;

      activatedRoute.data = of({ invoiceRole });
      comp.ngOnInit();

      expect(comp.invoicesSharedCollection()).toContainEqual(invoice);
      expect(comp.invoiceItemsSharedCollection()).toContainEqual(invoiceItem);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.invoiceRole).toEqual(invoiceRole);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceRole>();
      const invoiceRole = { id: 2077 };
      vi.spyOn(invoiceRoleFormService, 'getInvoiceRole').mockReturnValue(invoiceRole);
      vi.spyOn(invoiceRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceRole);
      saveSubject.complete();

      // THEN
      expect(invoiceRoleFormService.getInvoiceRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceRoleService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceRole));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceRole>();
      const invoiceRole = { id: 2077 };
      vi.spyOn(invoiceRoleFormService, 'getInvoiceRole').mockReturnValue({ id: null });
      vi.spyOn(invoiceRoleService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceRole);
      saveSubject.complete();

      // THEN
      expect(invoiceRoleFormService.getInvoiceRole).toHaveBeenCalled();
      expect(invoiceRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceRole>();
      const invoiceRole = { id: 2077 };
      vi.spyOn(invoiceRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInvoice', () => {
      it('should forward to invoiceService', () => {
        const entity = { id: 1362 };
        const entity2 = { id: 1801 };
        vi.spyOn(invoiceService, 'compareInvoice');
        comp.compareInvoice(entity, entity2);
        expect(invoiceService.compareInvoice).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInvoiceItem', () => {
      it('should forward to invoiceItemService', () => {
        const entity = { id: 29231 };
        const entity2 = { id: 28999 };
        vi.spyOn(invoiceItemService, 'compareInvoiceItem');
        comp.compareInvoiceItem(entity, entity2);
        expect(invoiceItemService.compareInvoiceItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoleType', () => {
      it('should forward to roleTypeService', () => {
        const entity = { id: 17576 };
        const entity2 = { id: 27052 };
        vi.spyOn(roleTypeService, 'compareRoleType');
        comp.compareRoleType(entity, entity2);
        expect(roleTypeService.compareRoleType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
