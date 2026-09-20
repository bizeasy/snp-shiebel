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

describe('TaxAuthorityRateType e2e test', () => {
  const taxAuthorityRateTypePageUrl = '/tax-authority-rate-type';
  let username: string;
  let password: string;
  const taxAuthorityRateTypeSample = { name: 'midwife' };

  let taxAuthorityRateType;
  let taxAuthority;
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
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/tax-authorities',
      body: { name: 'culture suckle' },
    }).then(({ body }) => {
      taxAuthority = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/tax-slabs',
      body: { name: 'whoever', rate: 27249.01 },
    }).then(({ body }) => {
      taxSlab = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tax-authority-rate-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tax-authority-rate-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tax-authority-rate-types/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/tax-authorities', {
      statusCode: 200,
      body: [taxAuthority],
    });

    cy.intercept('GET', '/api/tax-slabs', {
      statusCode: 200,
      body: [taxSlab],
    });
  });

  afterEach(() => {
    if (taxAuthorityRateType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-authority-rate-types/${taxAuthorityRateType.id}`,
      }).then(() => {
        taxAuthorityRateType = undefined;
      });
    }
  });

  afterEach(() => {
    if (taxAuthority) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-authorities/${taxAuthority.id}`,
      }).then(() => {
        taxAuthority = undefined;
      });
    }
    if (taxSlab) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-slabs/${taxSlab.id}`,
      }).then(() => {
        taxSlab = undefined;
      });
    }
  });

  it('TaxAuthorityRateTypes menu should load TaxAuthorityRateTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tax-authority-rate-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaxAuthorityRateType').should('exist');
    cy.location('pathname').should('eq', taxAuthorityRateTypePageUrl);
  });

  describe('TaxAuthorityRateType page', () => {
    it('should have translated page title', () => {
      cy.visit(taxAuthorityRateTypePageUrl);
      cy.getEntityHeading('TaxAuthorityRateType').should('not.contain', 'snpApp.taxAuthorityRateType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taxAuthorityRateTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaxAuthorityRateType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${taxAuthorityRateTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('TaxAuthorityRateType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityRateTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tax-authority-rate-types',
          body: {
            ...taxAuthorityRateTypeSample,
            taxAuthority,
            taxSlab,
          },
        }).then(({ body }) => {
          taxAuthorityRateType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tax-authority-rate-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [taxAuthorityRateType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(taxAuthorityRateTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaxAuthorityRateType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taxAuthorityRateType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityRateTypePageUrl);
      });

      it('edit button click should load edit TaxAuthorityRateType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxAuthorityRateType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityRateTypePageUrl);
      });

      it('edit button click should load edit TaxAuthorityRateType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxAuthorityRateType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityRateTypePageUrl);
      });

      it('last delete button click should delete instance of TaxAuthorityRateType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('taxAuthorityRateType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityRateTypePageUrl);

        taxAuthorityRateType = undefined;
      });
    });
  });

  describe('new TaxAuthorityRateType page', () => {
    beforeEach(() => {
      cy.visit(taxAuthorityRateTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TaxAuthorityRateType');
    });

    it('should create an instance of TaxAuthorityRateType', () => {
      cy.get(`[data-cy="name"]`).type('what inasmuch');
      cy.get(`[data-cy="name"]`).should('have.value', 'what inasmuch');

      cy.get(`[data-cy="description"]`).type('annex');
      cy.get(`[data-cy="description"]`).should('have.value', 'annex');

      cy.get(`[data-cy="taxAuthority"]`).select(1);
      cy.get(`[data-cy="taxSlab"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        taxAuthorityRateType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', taxAuthorityRateTypePageUrl);
    });
  });
});
