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

describe('UomConversion e2e test', () => {
  const uomConversionPageUrl = '/uom-conversion';
  let username: string;
  let password: string;
  const uomConversionSample = { name: 'fairly', conversionFactor: 12577.53 };

  let uomConversion;
  let uom;

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
      url: '/api/uoms',
      body: { name: 'unto but pace', description: 'psst', sequenceNo: 30354, abbreviation: 'unabashedl' },
    }).then(({ body }) => {
      uom = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/uom-conversions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/uom-conversions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/uom-conversions/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [uom],
    });
  });

  afterEach(() => {
    if (uomConversion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/uom-conversions/${uomConversion.id}`,
      }).then(() => {
        uomConversion = undefined;
      });
    }
  });

  afterEach(() => {
    if (uom) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/uoms/${uom.id}`,
      }).then(() => {
        uom = undefined;
      });
    }
  });

  it('UomConversions menu should load UomConversions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('uom-conversion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UomConversion').should('exist');
    cy.location('pathname').should('eq', uomConversionPageUrl);
  });

  describe('UomConversion page', () => {
    it('should have translated page title', () => {
      cy.visit(uomConversionPageUrl);
      cy.getEntityHeading('UomConversion').should('not.contain', 'snpApp.uomConversion.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(uomConversionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UomConversion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${uomConversionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('UomConversion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomConversionPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/uom-conversions',
          body: {
            ...uomConversionSample,
            fromUom: uom,
            toUom: uom,
          },
        }).then(({ body }) => {
          uomConversion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/uom-conversions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [uomConversion],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(uomConversionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UomConversion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('uomConversion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomConversionPageUrl);
      });

      it('edit button click should load edit UomConversion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UomConversion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomConversionPageUrl);
      });

      it('edit button click should load edit UomConversion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UomConversion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomConversionPageUrl);
      });

      it('last delete button click should delete instance of UomConversion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('uomConversion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomConversionPageUrl);

        uomConversion = undefined;
      });
    });
  });

  describe('new UomConversion page', () => {
    beforeEach(() => {
      cy.visit(uomConversionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UomConversion');
    });

    it('should create an instance of UomConversion', () => {
      cy.get(`[data-cy="name"]`).type('devil forearm certify');
      cy.get(`[data-cy="name"]`).should('have.value', 'devil forearm certify');

      cy.get(`[data-cy="conversionFactor"]`).type('16938.15');
      cy.get(`[data-cy="conversionFactor"]`).should('have.value', '16938.15');

      cy.get(`[data-cy="fromDate"]`).type('2023-12-13T22:14');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-13T22:14');

      cy.get(`[data-cy="toDate"]`).type('2023-12-14T00:40');
      cy.get(`[data-cy="toDate"]`).blur();
      cy.get(`[data-cy="toDate"]`).should('have.value', '2023-12-14T00:40');

      cy.get(`[data-cy="fromUom"]`).select(1);
      cy.get(`[data-cy="toUom"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        uomConversion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', uomConversionPageUrl);
    });
  });
});
