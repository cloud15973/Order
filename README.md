# Order
練習
主要資料表：member、product、orders、order_items、purchases

# 後台功能
會員管理：新增、修改、刪除會員資料
商品管理：CRUD + 圖片上傳
庫存管理：進貨與庫存量更新
銷售報表：Excel 匯出銷售統計
歷史訂單查詢：依日期與會員篩選

# 前台功能
訂單建立：即時扣庫存、交易控制
購物車管理：商品新增與移除
會員中心：更新個人資料、檢視訂單
歷史訂單查詢：檢視過去交易
評論系統：顧客可留下產品評價

# UI 設計（WindowBuilder）
BossMain、OrderMain 為主要視窗
採用 JPanel + CardLayout 結構

# 測試與驗證
資料庫：交易 rollback
報表：匯出 Members list 與 Sales summary
