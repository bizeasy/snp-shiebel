import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { ICustRequestType } from '../cust-request-type.model';
import { CustRequestTypeService } from '../service/cust-request-type.service';

import { CustRequestTypeFormService } from './cust-request-type-form.service';
import { CustRequestTypeUpdate } from './cust-request-type-update';

describe('CustRequestType Management Update Component', () => {
  let comp: CustRequestTypeUpdate;
  let fixture: ComponentFixture<CustRequestTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let custRequestTypeFormService: CustRequestTypeFormService;
  let custRequestTypeService: CustRequestTypeService;
  let partyService: PartyService;

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

    fixture = TestBed.createComponent(CustRequestTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    custRequestTypeFormService = TestBed.inject(CustRequestTypeFormService);
    custRequestTypeService = TestBed.inject(CustRequestTypeService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CustRequestType query and add missing value', () => {
      const custRequestType: ICustRequestType = { id: 951 };
      const parent: ICustRequestType = { id: 4424 };
      custRequestType.parent = parent;

      const custRequestTypeCollection: ICustRequestType[] = [{ id: 4424 }];
      vi.spyOn(custRequestTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestTypeCollection })));
      const additionalCustRequestTypes = [parent];
      const expectedCollection: ICustRequestType[] = [...additionalCustRequestTypes, ...custRequestTypeCollection];
      vi.spyOn(custRequestTypeService, 'addCustRequestTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestType });
      comp.ngOnInit();

      expect(custRequestTypeService.query).toHaveBeenCalled();
      expect(custRequestTypeService.addCustRequestTypeToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestTypeCollection,
        ...additionalCustRequestTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const custRequestType: ICustRequestType = { id: 951 };
      const party: IParty = { id: 24527 };
      custRequestType.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestType });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const custRequestType: ICustRequestType = { id: 951 };
      const parent: ICustRequestType = { id: 4424 };
      custRequestType.parent = parent;
      const party: IParty = { id: 24527 };
      custRequestType.party = party;

      activatedRoute.data = of({ custRequestType });
      comp.ngOnInit();

      expect(comp.custRequestTypesSharedCollection()).toContainEqual(parent);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.custRequestType).toEqual(custRequestType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestType>();
      const custRequestType = { id: 4424 };
      vi.spyOn(custRequestTypeFormService, 'getCustRequestType').mockReturnValue(custRequestType);
      vi.spyOn(custRequestTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestType);
      saveSubject.complete();

      // THEN
      expect(custRequestTypeFormService.getCustRequestType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(custRequestTypeService.update).toHaveBeenCalledWith(expect.objectContaining(custRequestType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestType>();
      const custRequestType = { id: 4424 };
      vi.spyOn(custRequestTypeFormService, 'getCustRequestType').mockReturnValue({ id: null });
      vi.spyOn(custRequestTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestType);
      saveSubject.complete();

      // THEN
      expect(custRequestTypeFormService.getCustRequestType).toHaveBeenCalled();
      expect(custRequestTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestType>();
      const custRequestType = { id: 4424 };
      vi.spyOn(custRequestTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(custRequestTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustRequestType', () => {
      it('should forward to custRequestTypeService', () => {
        const entity = { id: 4424 };
        const entity2 = { id: 951 };
        vi.spyOn(custRequestTypeService, 'compareCustRequestType');
        comp.compareCustRequestType(entity, entity2);
        expect(custRequestTypeService.compareCustRequestType).toHaveBeenCalledWith(entity, entity2);
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
  });
});
