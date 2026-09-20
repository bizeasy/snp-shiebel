import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('TaxSlab e2e test', () => {
  const taxSlabPageUrl = '/tax-slab';
  let username: string;
  let password: string;
  const taxSlabSample = { name: 'mortally fondly unimporta', rate: 16598.12 };

  let taxSlab;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tax-slabs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tax-slabs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tax-slabs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (taxSlab) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-slabs/${taxSlab.id}`,
      }).then(() => {
        taxSlab = undefined;
      });
    }
  });

  it('TaxSlabs menu should load TaxSlabs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tax-slab');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaxSlab').should('exist');
    cy.location('pathname').should('eq', taxSlabPageUrl);
  });

  describe('TaxSlab page', () => {
    it('should have translated page title', () => {
      cy.visit(taxSlabPageUrl);
      cy.getEntityHeading('TaxSlab').should('not.contain', 'snpApp.taxSlab.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taxSlabPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaxSlab page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${taxSlabPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('TaxSlab');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxSlabPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tax-slabs',
          body: taxSlabSample,
        }).then(({ body }) => {
          taxSlab = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tax-slabs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [taxSlab],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(taxSlabPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaxSlab page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taxSlab');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxSlabPageUrl);
      });

      it('edit button click should load edit TaxSlab page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxSlab');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxSlabPageUrl);
      });

      it('edit button click should load edit TaxSlab page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxSlab');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxSlabPageUrl);
      });

      it('last delete button click should delete instance of TaxSlab', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('taxSlab').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxSlabPageUrl);

        taxSlab = undefined;
      });
    });
  });

  describe('new TaxSlab page', () => {
    beforeEach(() => {
      cy.visit(taxSlabPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TaxSlab');
    });

    it('should create an instance of TaxSlab', () => {
      cy.get(`[data-cy="name"]`).type('catalyze');
      cy.get(`[data-cy="name"]`).should('have.value', 'catalyze');

      cy.get(`[data-cy="rate"]`).type('8812.24');
      cy.get(`[data-cy="rate"]`).should('have.value', '8812.24');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        taxSlab = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', taxSlabPageUrl);
    });
  });
});
